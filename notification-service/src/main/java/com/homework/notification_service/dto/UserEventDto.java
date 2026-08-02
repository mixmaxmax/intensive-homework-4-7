package com.homework.notification_service.dto;

import com.homework.notification_service.enums.OperationType;
import lombok.Data;

@Data
public class UserEventDto {
    private OperationType operationType;
    private String email;
}