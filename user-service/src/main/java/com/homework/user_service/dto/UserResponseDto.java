package com.homework.user_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    @Schema(description = "ID пользователя")
    private Integer id;

    @Schema(description = "Имя пользователя")
    @NotBlank
    private String name;

    @Schema(description = "Email пользователя")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Возраст пользователя")
    @Positive
    private Integer age;

    @Schema(description = "Дата и время добавления пользователя в БД")
    private LocalDateTime createdAt;
}
