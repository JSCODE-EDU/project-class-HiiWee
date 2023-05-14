package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidTitleException extends BadRequestException {

    private static final String MESSAGE = "게시글 제목은 1자 이상 15자 이하까지 입력할 수 있습니다.";

    public InvalidTitleException() {
        super(MESSAGE, PostErrorCode.INVALID_TITLE.value());
    }
}
