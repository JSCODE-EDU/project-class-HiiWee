package com.example.anonymousboard.post.domain;

import com.example.anonymousboard.post.exception.InvalidPostKeywordException;

public class Keyword {

    private static final int MINIMUM_LENGTH = 1;

    private final String value;

    private Keyword(final String keyword) {
        this.value = keyword;
    }

    public static Keyword createValidKeyword(final String keyword) {
        if (keyword.trim().length() >= MINIMUM_LENGTH) {
            return new Keyword(keyword.trim());
        }
        throw new InvalidPostKeywordException();
    }

    public String getValue() {
        return value;
    }
}
