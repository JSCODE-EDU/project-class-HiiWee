package com.example.anonymousboard.advice;

public class InternalServerException extends RuntimeException {

    private final int errorCode;

    public InternalServerException(final String message, final int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
