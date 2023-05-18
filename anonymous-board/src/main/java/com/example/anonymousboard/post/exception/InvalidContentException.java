package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidContentException extends BadRequestException {

    private static final String MESSAGE = "게시글 내용은 1자 이상 1000자 이하까지 입력할 수 있습니다.";

    public InvalidContentException() {
        super(MESSAGE, PostErrorCode.INVALID_CONTENT.value());
    }
}
