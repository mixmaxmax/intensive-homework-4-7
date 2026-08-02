package com.homework.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.time.LocalDateTime;


@Data
public class UserRequestDto {
    private Integer id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @Positive
    private Integer age;

    private LocalDateTime createdAt;
}

