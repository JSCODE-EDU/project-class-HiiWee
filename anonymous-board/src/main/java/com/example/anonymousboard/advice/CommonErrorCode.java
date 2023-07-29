package com.example.anonymousboard.advice;

public enum CommonErrorCode {

    RUNTIME(-1000),
    METHOD_ARGUMENT_NOT_VALID(-2000),
    BIND_FILED_NOT_VALUE(-3000);

    private final int errorCode;

    CommonErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
