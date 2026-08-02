package com.homework.notification_service.integration;

import com.homework.notification_service.dto.UserEventDto;
import com.homework.notification_service.enums.OperationType;
import com.homework.notification_service.service.KafkaConsumerService;
import com.homework.notification_service.service.MailSenderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
        "mail.recipient.test=testRecipient",
        "spring.kafka.bootstrap-servers=localhost:9092",
})
class KafkaConsumerServiceTest {

    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @MockitoBean
    private MailSenderService mailSenderService;

    @Test
    void sendEmailCreate_Success() {
        UserEventDto dto = new UserEventDto();
        dto.setOperationType(OperationType.CREATE);
        dto.setEmail("user@mail.com");

        kafkaConsumerService.consumerUserCreate(dto);

        ArgumentCaptor<String> recipient = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);

        verify(mailSenderService).sendMail(
            recipient.capture(),
            subject.capture(),
            body.capture()
        );

        assertThat(recipient.getValue()).isEqualTo("testRecipient");
        assertThat(subject.getValue()).isEqualTo("Создание аккаунта");
        assertThat(body.getValue())
            .contains("user@mail.com")
            .contains("Вы успешно создали аккаунт");
    }

    @Test
    void sendEmailDeleteSuccess() {
        UserEventDto dto = new UserEventDto();
        dto.setOperationType(OperationType.DELETE);
        dto.setEmail("user@mail.com");

        kafkaConsumerService.consumerUserDeleted(dto);

        ArgumentCaptor<String> recipient = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);

        verify(mailSenderService).sendMail(
            recipient.capture(),
            subject.capture(),
            body.capture()
        );

        assertThat(recipient.getValue()).isEqualTo("testRecipient");
        assertThat(subject.getValue()).isEqualTo("Удаление аккаунта");
        assertThat(body.getValue())
            .contains("user@mail.com")
            .contains("Вы успешно удалили аккаунт");
    }
}