package com.example.anonymousboard.advice;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;
    private final int errorCode;

    @Builder
    private ErrorResponse(final String message, final int errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }
}
