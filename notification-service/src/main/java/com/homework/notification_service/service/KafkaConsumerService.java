package com.homework.notification_service.service;

import com.homework.notification_service.dto.UserEventDto;
import com.homework.notification_service.exception.SendingMailException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MailSenderService mailSenderService;
    private final Validator validator;

    @KafkaListener(topics = "user-created", groupId = "notification-group")
    public void consumerUserCreate(UserEventDto userEventDto) {
        if (!userEventDtoIsValid(userEventDto)) {
            log.error("Отправка сообщения о создании пользователя прервана! userEventDto невалиден: {}", userEventDto.toString());
            return;
        }
        try {
            log.info("Создан новый пользователь. Почтовый адрес пользователя={}", userEventDto.getEmail());
            mailSenderService.sendMail(
                    userEventDto.getEmail(),
                    "Создание аккаунта",
                    "Здравствуйте! Ваш аккаунт на сайте www.site.com был успешно создан."
            );
        } catch (SendingMailException e) {
            log.error("Ошибка при отправке письма о создании аккаунта получателю={}. Ошибка: {}", userEventDto.getEmail(), e.getMessage());
        }
    }

    @KafkaListener(topics = "user-deleted", groupId = "notification-group")
    public void consumerUserDeleted(UserEventDto userEventDto) {
        if (!userEventDtoIsValid(userEventDto)) {
            log.error("Отправка сообщения об удалении пользователя прервана! userEventDto невалиден: {}", userEventDto.toString());
            return;
        }
        try {
            log.info("Удален пользователь={}", userEventDto.getEmail());
            mailSenderService.sendMail(
                    userEventDto.getEmail(),
                    "Удаление аккаунта",
                    "Здравствуйте! Ваш аккаунт был удалён."
            );
        } catch (SendingMailException e) {
            log.error("Ошибка при отправке письма об удалении аккаунта получателю={}. Ошибка: {}", userEventDto.getEmail(), e.getMessage());
        }
    }

    /*Валидация userEventDto при отправке в KafkaListener*/
    private boolean userEventDtoIsValid (UserEventDto userEventDto) {
        return userEventDto != null && validator.validate(userEventDto).isEmpty();
    }
}