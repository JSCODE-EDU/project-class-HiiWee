package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidContentException extends BadRequestException {

    private static final String MESSAGE = "게시글 본문은 5000자 이하여야 합니다.";

    public InvalidContentException() {
        super(MESSAGE);
    }
}
