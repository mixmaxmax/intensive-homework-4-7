package com.homework.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class UserRequestDto {

    @Schema(description = "ФИО пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "Email пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Возраст пользователя", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive
    private Integer age;

}

