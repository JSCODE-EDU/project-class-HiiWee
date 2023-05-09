package com.example.anonymousboard.advice;

public class BadRequestException extends BusinessException {

    public BadRequestException(final String message) {
        super(message);
    }
}
