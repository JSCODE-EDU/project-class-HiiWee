package com.example.anonymousboard.comment.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidCommentException extends BadRequestException {

    private static final String MESSAGE = "댓글은 1자 이상 50자 이하까지 작성할 수 있습니다";

    public InvalidCommentException() {
        super(MESSAGE, CommentErrorCode.INVALID_COMMENT.value());
    }
}
