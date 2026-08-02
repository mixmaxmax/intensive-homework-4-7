package com.homework.notification_service.exception;

public class SendingMailException extends RuntimeException{
    public SendingMailException (String message) {
        super(message);
    }
}
