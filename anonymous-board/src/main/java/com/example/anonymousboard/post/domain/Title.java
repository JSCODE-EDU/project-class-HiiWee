package com.example.anonymousboard.post.domain;

import com.example.anonymousboard.post.exception.InvalidTitleException;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Title {

    private static final int LIMIT_LENGTH = 200;

    @Column(name = "title")
    private String value;

    protected Title() {
    }

    public Title(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value.length() > LIMIT_LENGTH) {
            throw new InvalidTitleException();
        }
    }
}
