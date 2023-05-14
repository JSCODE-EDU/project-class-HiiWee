package com.example.anonymousboard.post.exception;

import com.example.anonymousboard.advice.BadRequestException;

public class InvalidPostKeywordException extends BadRequestException {

    private static final String MESSAGE = "검색 키워드는 공백을 입력할 수 없으며, 1글자 이상 입력해야 합니다.";

    public InvalidPostKeywordException() {
        super(MESSAGE, PostErrorCode.INVALID_POST_KEYWORD.value());
    }
}
