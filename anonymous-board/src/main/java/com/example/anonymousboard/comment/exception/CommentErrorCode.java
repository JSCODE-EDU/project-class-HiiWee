package com.example.anonymousboard.comment.exception;

public enum CommentErrorCode {
    INVALID_COMMENT(4001),
    COMMENT_LIMIT(4002);

    private final int errorCode;

    CommentErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
