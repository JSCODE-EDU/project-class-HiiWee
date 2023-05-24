package com.example.anonymousboard.member.exception;

import com.example.anonymousboard.advice.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = "회원을 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE, MemberErrorCode.MEMBER_NOT_FOUND.value());
    }
}
