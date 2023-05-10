package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidTitleException extends BadRequestException {

    private static final String MESSAGE = "게시글 제목은 200자 이하여야 합니다.";

    public InvalidTitleException() {
        super(MESSAGE, PostErrorCode.INVALID_TITLE.value());
    }
}
