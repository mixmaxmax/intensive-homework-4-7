package com.homework.notification_service.controller;

import com.homework.notification_service.dto.UserEventDto;
import com.homework.notification_service.service.MailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notifications")
@Slf4j
public class NotificationController {

    private final MailSenderService mailSenderService;
    private final String testRecipient;

    private NotificationController(MailSenderService mailSenderService, @Value("${mail.recipient.test}") String testRecipient) {
        this.mailSenderService = mailSenderService;
        this.testRecipient = testRecipient;
    }

    @PostMapping
    private void sendNotification(@RequestBody UserEventDto userEventDto) {
        if (userEventDto.getOperationType()==null || userEventDto.getEmail()==null) {
            throw new NullPointerException("One of fields of UserEventDto object is null!!!");
        } else {
            String title, content;

            switch (userEventDto.getOperationType()) {
                case CREATE:
                    title = "Создание аккаунта";
                    content = "Здравствуйте " + userEventDto.getEmail() + ". Вы успешно создали аккаунт user-service";
                    break;

                case DELETE:
                    title = "Удаление аккаунта";
                    content = "Здравствуйте " + userEventDto.getEmail() + ". Вы успешно удалили аккаунт user-service";
                    break;

                default:
                    throw new IllegalArgumentException("Illegal operation type: " + userEventDto.getOperationType());
            }

            mailSenderService.sendMail(testRecipient, title, content);
            log.info("Отправлено сообщение через API. Получатель={}, заголовок={}, содержимое={}.", userEventDto.getEmail(), title, content);
        }

    }
}