package com.example.anonymousboard.auth.exception;

import com.example.anonymousboard.advice.UnauthorizedException;

public class LoginFailedException extends UnauthorizedException {

    private static final String MESSAGE = "아이디 혹은 비밀번호가 잘못되었습니다.";

    public LoginFailedException() {
        super(MESSAGE, AuthErrorCode.LOGIN_FAILED.value());
    }
}
