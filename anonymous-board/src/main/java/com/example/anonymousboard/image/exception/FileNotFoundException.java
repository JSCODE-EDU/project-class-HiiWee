package com.example.anonymousboard.image.exception;

import com.example.anonymousboard.advice.NotFoundException;

public class FileNotFoundException extends NotFoundException {

    private static final String MESSAGE = "파일을 찾을 수 없습니다.";

    public FileNotFoundException() {
        super(MESSAGE, FileErrorCode.FILE_NOT_FOUND.value());
    }
}
