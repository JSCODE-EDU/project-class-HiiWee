package com.example.anonymousboard.advice;

public class ForbiddenException extends BusinessException {

    private final int errorCode;

    public ForbiddenException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
