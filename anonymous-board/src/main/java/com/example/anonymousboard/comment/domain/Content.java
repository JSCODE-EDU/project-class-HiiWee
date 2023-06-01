package com.example.anonymousboard.comment.domain;

import com.example.anonymousboard.comment.exception.InvalidCommentException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class Content {

    private static final int LIMIT_LENGTH = 50;
    @Column(name = "content")
    private String value;

    protected Content() {
    }

    private Content(final String value) {
        this.value = value;
    }

    public static Content from(final String value) {
        String trimValue = value.trim();
        validate(trimValue);
        return new Content(trimValue);
    }

    private static void validate(final String trimValue) {
        if (trimValue.isEmpty() || trimValue.length() > LIMIT_LENGTH) {
            throw new InvalidCommentException();
        }
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
