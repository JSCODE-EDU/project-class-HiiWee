package com.example.anonymousboard.post.domain;

public class Keyword {

    private static final int MINIMUM_LENGTH = 2;

    private final String value;

    private Keyword(final String keyword) {
        this.value = keyword;
    }

    public static Keyword createValidKeyword(final String keyword) {
        if (keyword != null && !"".equals(keyword.trim()) && keyword.length() >= MINIMUM_LENGTH) {
            return new Keyword(String.format("%%%s%%", keyword));
        }
        return null;
    }

    public String getValue() {
        return value;
    }

}
