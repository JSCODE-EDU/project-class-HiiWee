package com.example.anonymousboard.post.exception;

public enum PostErrorCode {

    INVALID_TITLE(1001),
    INVALID_CONTENT(1002);

    private final int errorCode;

    PostErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
