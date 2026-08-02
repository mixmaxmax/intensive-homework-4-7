package com.homework.user_service.dto;

import com.homework.user_service.enums.OperationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class UserEventDto {
    private OperationType operationType;
    private String email;
}