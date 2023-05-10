package com.example.anonymousboard.post.domain;

public class Keyword {

    private static final int MINIMUM_LENGTH = 2;

    private final String keyword;

    private Keyword(final String keyword) {
        this.keyword = keyword;
    }

    public static Keyword createValidKeyword(final String keyword) {
        if (keyword != null && !"".equals(keyword.trim()) && keyword.length() >= MINIMUM_LENGTH) {
            return new Keyword(keyword);
        }
        return null;
    }

    public String getQueryKeyword() {
        return String.format("%%%s%%", keyword);
    }

}
