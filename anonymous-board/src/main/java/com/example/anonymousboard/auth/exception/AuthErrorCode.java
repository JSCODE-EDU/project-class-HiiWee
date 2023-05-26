package com.example.anonymousboard.auth.exception;

public enum AuthErrorCode {

    LOGIN_FAILED(3001),

    // JWT 관련 코드
    NO_AUTHORIZATION_HEADER(3002),
    INVALID_HEADER_FORMAT(3003),
    UNSUPPORTED_JWT(3004),
    EXPIRED_JWT(3005),
    MALFORMED_JWT(3006),
    INVALID_SIGNATURE(3007);

    private final int errorCode;

    AuthErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
