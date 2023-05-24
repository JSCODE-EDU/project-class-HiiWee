package com.example.anonymousboard.auth.dto;

import lombok.Getter;

@Getter
public class AuthInfo {

    private final Long id;

    private AuthInfo(final Long id) {
        this.id = id;
    }

    public static AuthInfo from(final Object idObject) {
        return new AuthInfo((Long) idObject);
    }
}
