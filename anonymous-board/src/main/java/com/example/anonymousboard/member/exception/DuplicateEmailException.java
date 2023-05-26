package com.example.anonymousboard.member.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class DuplicateEmailException extends BadRequestException {

    private static final String MESSAGE = "이미 사용중인 이메일 입니다.";

    public DuplicateEmailException() {
        super(MESSAGE, MemberErrorCode.DUPLICATE_EMAIL.value());
    }
}
