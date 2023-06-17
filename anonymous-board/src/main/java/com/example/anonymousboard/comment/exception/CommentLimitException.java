package com.example.anonymousboard.comment.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class CommentLimitException extends BadRequestException {

    private static final String MESSAGE = "전체 게시글 조회시 댓글은 최대 100개까지 조회할 수 있습니다.";

    public CommentLimitException() {
        super(MESSAGE, CommentErrorCode.COMMENT_LIMIT.value());
    }
}
