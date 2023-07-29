package com.example.anonymousboard.image.exception;

public enum FileErrorCode {

    FILE_NOT_FOUND(5001),
    FILE_UPLOAD_FAIL(5002),
    FILE_SIZE_LIMIT(5003);

    private final int errorCode;

    FileErrorCode(final int errorCode) {
        this.errorCode = errorCode;
    }

    public int value() {
        return errorCode;
    }
}
