package com.example.anonymousboard.member.exception;

public enum MemberErrorCode {

    INVALID_EMAIL_FORMAT(2001),
    INVALID_PASSWORD_FORMAT(2002),
    DUPLICATE_EMAIL(2003),
    INVALID_PASSWORD_CONFIRMATION(2004);

    private final int errorCode;

    MemberErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
