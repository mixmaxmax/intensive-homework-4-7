package com.homework.user_service.dto;

import lombok.Setter;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private LocalDateTime createdAt;
}
