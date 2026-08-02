package com.homework.notification_service.service;

import com.homework.notification_service.exception.SendingMailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailSenderService {

    private final JavaMailSender mailSender;
    private final String sender;

    public MailSenderService(JavaMailSender mailSender, @Value("${spring.mail.username}") String sender) {
        this.mailSender = mailSender;
        this.sender = sender;
    }

    public void sendMail(String recipient, String title, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(recipient);
        message.setSubject(title);
        message.setText(content);

        try{
            mailSender.send(message);
            log.info("Отправлено сообщение через MailSenderService. Получатель={}, заголовок={}, содержимое={}.", recipient, title, content);
        } catch (MailException e) {
            log.error("Ошибка при отправке письма получателю={}. Ошибка: {}", recipient, e.getMessage());
            throw new SendingMailException("Ошибка при отправке письма получателю=" + recipient + ". Ошибка: " + e.getMessage());
        }
    }
}