package com.example.anonymousboard.auth.dto;

import lombok.Getter;

@Getter
public class AuthInfo {

    private final Long id;

    public AuthInfo(final Long id) {
        this.id = id;
    }

    public static AuthInfo from(final Object idObject) {
        int id = (int) idObject;
        return new AuthInfo((long) id);
    }
}
