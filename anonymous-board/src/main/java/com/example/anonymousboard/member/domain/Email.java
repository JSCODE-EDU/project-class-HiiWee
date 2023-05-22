package com.example.anonymousboard.member.domain;

import com.example.anonymousboard.member.exception.InvalidEmailFormatException;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email {

    private static final Pattern PATTERN = Pattern.compile("^[a-z]{1}[a-z0-9_\\.]+@[a-z\\.]+\\.[a-zA-Z]+$");

    @Column(name = "email")
    private String value;

    protected Email() {
    }

    private Email(final String value) {
        this.value = value;
    }

    public static Email from(final String value) {
        validate(value);
        return new Email(value);
    }

    private static void validate(final String email) {
        if (!PATTERN.matcher(email).matches()) {
            throw new InvalidEmailFormatException();
        }
    }

    public String getValue() {
        return value;
    }
}
