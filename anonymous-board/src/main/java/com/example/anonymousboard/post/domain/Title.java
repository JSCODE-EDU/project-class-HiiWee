package com.example.anonymousboard.post.domain;

import com.example.anonymousboard.post.exception.InvalidTitleException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    private static final int LIMIT_LENGTH = 15;

    @Column(name = "title")
    private String value;

    protected Title() {
    }

    private Title(final String value) {
        this.value = value;
    }

    public static Title from(final String value) {
        String trimValue = value.trim();
        validate(trimValue);
        return new Title(trimValue);
    }

    private static void validate(final String value) {
        if (value.trim().length() > LIMIT_LENGTH) {
            throw new InvalidTitleException();
        }
    }

    public String getValue() {
        return value;
    }
}
