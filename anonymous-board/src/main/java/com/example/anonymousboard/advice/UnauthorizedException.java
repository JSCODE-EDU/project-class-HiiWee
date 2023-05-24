package com.example.anonymousboard.advice;

public class UnauthorizedException extends BusinessException {

    private final int errorCode;

    public UnauthorizedException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
