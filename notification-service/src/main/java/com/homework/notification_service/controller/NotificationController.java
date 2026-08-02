package com.homework.notification_service.controller;

import com.homework.notification_service.dto.UserEventDto;
import com.homework.notification_service.service.MailSenderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/notifications")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final MailSenderService mailSenderService;

    @PostMapping
    private void sendNotification(@Valid @RequestBody UserEventDto userEventDto) {
        if (userEventDto.getOperationType()==null || userEventDto.getEmail()==null) {
            throw new NullPointerException("One of fields of UserEventDto object is null!!!");
        } else {
            String title, content;

            switch (userEventDto.getOperationType()) {
                case CREATE:
                    title = "Создание аккаунта";
                    content = "Здравствуйте! Ваш аккаунт на сайте www.site.com был успешно создан.";
                    break;

                case DELETE:
                    title = "Удаление аккаунта";
                    content = "Здравствуйте! Ваш аккаунт был удалён.";
                    break;

                default:
                    throw new IllegalArgumentException("Illegal operation type: " + userEventDto.getOperationType());
            }

            mailSenderService.sendMail(userEventDto.getEmail(), title, content);
            log.info("Отправлено сообщение через API. Получатель={}, заголовок={}, содержимое={}.", userEventDto.getEmail(), title, content);
        }

    }
}