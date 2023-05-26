package com.example.anonymousboard.member.domain;

import com.example.anonymousboard.member.exception.InvalidPasswordFormatException;
import java.util.Objects;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Password {

    private static final Pattern PATTERN = Pattern.compile(
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$");

    @Column(name = "password")
    private String value;

    protected Password() {
    }

    private Password(final String value) {
        this.value = value;
    }

    public static Password from(final String value) {
        validate(value);
        return new Password(value);
    }

    private static void validate(final String value) {
        if (!PATTERN.matcher(value).matches()) {
            throw new InvalidPasswordFormatException();
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
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
