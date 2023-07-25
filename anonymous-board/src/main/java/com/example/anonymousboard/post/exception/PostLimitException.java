package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class PostLimitException extends BadRequestException {

    private static final String MESSAGE = "게시글은 최대 100개까지 조회할 수 있습니다.";

    public PostLimitException() {
        super(MESSAGE, PostErrorCode.POST_LIMIT.value());
    }
}
