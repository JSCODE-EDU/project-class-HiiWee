package com.example.anonymousboard.advice;

public class NotFoundException extends BusinessException {

    private final int errorCode;

    public NotFoundException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}

