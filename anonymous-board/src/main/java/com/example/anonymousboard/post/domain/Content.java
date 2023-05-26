package com.example.anonymousboard.post.domain;

import com.example.anonymousboard.post.exception.InvalidContentException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
public class Content {

    private static final int LIMIT_LENGTH = 1000;

    @Lob
    @Column(name = "content")
    private String value;

    protected Content() {
    }

    private Content(final String value) {
        this.value = value;
    }

    public static Content from(final String value) {
        validate(value);
        return new Content(value);
    }

    private static void validate(final String value) {
        if (value.length() > LIMIT_LENGTH || value.length() == 0) {
            throw new InvalidContentException();
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Content content = (Content) o;
        return Objects.equals(value, content.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
