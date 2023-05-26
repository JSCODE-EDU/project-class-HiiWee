package com.example.anonymousboard.member.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidPasswordFormatException extends BadRequestException {

    private static final String MESSAGE = "패스워드는 최소 8자이상 ~ 15자이하 까지 이며 하나 이상의 영문, 숫자, 특수 문자(@$!%*#?&)를 포함해야 합니다.";

    public InvalidPasswordFormatException() {
        super(MESSAGE, MemberErrorCode.INVALID_PASSWORD_FORMAT.value());
    }
}
