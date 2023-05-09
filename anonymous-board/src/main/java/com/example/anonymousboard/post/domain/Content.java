package com.example.anonymousboard.post.domain;

import com.example.anonymousboard.post.exception.InvalidContentException;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Content {

    private static final int LIMIT_LENGTH = 5000;

    @Lob
    @Column(name = "content")
    private String value;

    protected Content() {
    }

    public Content(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (value.length() > LIMIT_LENGTH) {
            throw new InvalidContentException();
        }
    }
}
