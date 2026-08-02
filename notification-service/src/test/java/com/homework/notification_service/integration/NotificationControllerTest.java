package com.homework.notification_service.integration;

import com.homework.notification_service.controller.NotificationController;
import com.homework.notification_service.dto.UserEventDto;
import com.homework.notification_service.enums.OperationType;
import com.homework.notification_service.service.MailSenderService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MailSenderService mailSenderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void senEmailCreate_Success() throws Exception {
        UserEventDto dto = new UserEventDto();
        dto.setOperationType(OperationType.CREATE);
        dto.setEmail("user@mail.com");

        mockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(200));

        ArgumentCaptor<String> recipient = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);

        verify(mailSenderService).sendMail(
            recipient.capture(),
            subject.capture(),
            body.capture()
        );

        assertThat(recipient.getValue()).isEqualTo(dto.getEmail());
        assertThat(subject.getValue()).isEqualTo("Создание аккаунта");
        assertThat(body.getValue()).contains("Здравствуйте! Ваш аккаунт на сайте www.site.com был успешно создан.");
    }
    @Test
    void sendEmailDelete_Success() throws Exception {
        UserEventDto dto = new UserEventDto();
        dto.setOperationType(OperationType.DELETE);
        dto.setEmail("user@mail.com");

        mockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(result -> assertThat(result.getResponse().getStatus()).isEqualTo(200));

        ArgumentCaptor<String> recipient = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> body = ArgumentCaptor.forClass(String.class);

        verify(mailSenderService).sendMail(
            recipient.capture(),
                subject.capture(),
                body.capture()
        );
        assertThat(recipient.getValue()).isEqualTo(dto.getEmail());
        assertThat(subject.getValue()).isEqualTo("Удаление аккаунта");
        assertThat(body.getValue()).contains("Здравствуйте! Ваш аккаунт был удалён.");
    }

    @Test
    void OperationTypeFieldIsNull_Error() {
        UserEventDto dto = new UserEventDto();
        dto.setEmail("user@mail.com");

        Exception exception = assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
        );

        assertThat(exception.getCause()).isInstanceOf(NullPointerException.class);

        verify(mailSenderService, never()).sendMail(anyString(), anyString(), anyString());
    }

    @Test
    void EmailFieldIsNull_Error() {
        UserEventDto dto = new UserEventDto();
        dto.setOperationType(OperationType.CREATE);

        Exception exception = assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
        );

        assertThat(exception.getCause()).isInstanceOf(NullPointerException.class);

        verify(mailSenderService, never()).sendMail(anyString(), anyString(), anyString());
    }
}