package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    private static final String MESSAGE = "게시글을 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(MESSAGE, PostErrorCode.POST_NOT_FOUND.value());
    }
}
