package com.example.anonymousboard.post.exception;

public enum PostErrorCode {

    INVALID_TITLE(1001),
    INVALID_CONTENT(1002),
    POST_NOT_FOUND(1003),
    INVALID_POST_KEYWORD(1004);

    private final int errorCode;

    PostErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
