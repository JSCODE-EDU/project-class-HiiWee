package com.example.anonymousboard.auth.exception;

public enum AuthErrorCode {

    LOGIN_FAILED(3001);

    private final int errorCode;

    AuthErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
