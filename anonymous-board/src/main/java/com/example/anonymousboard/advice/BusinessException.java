package com.example.anonymousboard.advice;

public class BusinessException extends RuntimeException {

    public BusinessException(final String message) {
        super(message);
    }
}
