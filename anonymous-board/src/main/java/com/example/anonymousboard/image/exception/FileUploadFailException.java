package com.example.anonymousboard.image.exception;

import com.example.anonymousboard.advice.InternalServerException;

public class FileUploadFailException extends InternalServerException {

    private static final String MESSAGE = "파일 업로드를 실패했습니다.";

    public FileUploadFailException() {
        super(MESSAGE, FileErrorCode.FILE_UPLOAD_FAIL.value());
    }
}
