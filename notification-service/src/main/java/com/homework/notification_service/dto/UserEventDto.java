package com.homework.notification_service.dto;

import com.homework.notification_service.enums.OperationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserEventDto {

    @NotNull
    private OperationType operationType;

    @Email
    @NotBlank
    private String email;
}