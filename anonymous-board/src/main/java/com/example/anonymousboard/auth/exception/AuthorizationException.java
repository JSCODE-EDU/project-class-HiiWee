package com.example.anonymousboard.auth.exception;

import com.example.anonymousboard.advice.ForbiddenException;

public class AuthorizationException extends ForbiddenException {

    private static final String MESSAGE = "권한이 없습니다.";

    public AuthorizationException() {
        super(MESSAGE, AuthErrorCode.AUTHORIZATION.value());
    }
}
