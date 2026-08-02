package com.homework.notification_service.service;

import com.homework.notification_service.dto.UserEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    private final MailSenderService mailSenderService;
    private final String testRecipient;

    public KafkaConsumerService(MailSenderService mailSenderService, @Value("${mail.recipient.test}") String testRecipient) {
        this.mailSenderService = mailSenderService;
        this.testRecipient = testRecipient;
    }

    @KafkaListener(topics = "user-created", groupId = "notification-group")
    public void consumerUserCreate(UserEventDto userEventDto) {
        log.info("Создан новый пользователь. Почтовый адрес пользователя={}", userEventDto.getEmail());
        mailSenderService.sendMail(
               testRecipient,
                "Создание аккаунта",
                "Здравствуйте, " + userEventDto.getEmail() + ". Вы успешно создали аккаунт user-service."
        );
    }

    @KafkaListener(topics = "user-deleted", groupId = "notification-group")
    public void consumerUserDeleted(UserEventDto userEventDto) {
        log.info("Удален пользователь={}", userEventDto.getEmail());
        mailSenderService.sendMail(
                testRecipient,
                "Удаление аккаунта",
                "Здравствуйте " + userEventDto.getEmail() +". Вы успешно удалили аккаунт user-service."
        );
    }
}