package com.example.anonymousboard.member.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidEmailFormatException extends BadRequestException {

    private static final String MESSAGE = "잘못된 이메일 형식입니다! (이메일 아이디는 영어로 시작해야 하며, 하나의 '@'를 포함하고 있어야 합니다.";

    public InvalidEmailFormatException() {
        super(MESSAGE, MemberErrorCode.INVALID_EMAIL_FORMAT.value());
    }
}
