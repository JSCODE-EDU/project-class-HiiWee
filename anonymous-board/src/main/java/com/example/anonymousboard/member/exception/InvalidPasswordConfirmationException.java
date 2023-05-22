package com.example.anonymousboard.member.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidPasswordConfirmationException extends BadRequestException {

    private static final String MESSAGE = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";

    public InvalidPasswordConfirmationException() {
        super(MESSAGE, MemberErrorCode.INVALID_PASSWORD_CONFIRMATION.value());
    }
}
