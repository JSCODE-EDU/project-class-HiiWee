package com.example.anonymousboard.auth.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private final String token;

    private TokenResponse(final String token) {
        this.token = token;
    }

    public static TokenResponse from(final String token) {
        return new TokenResponse(token);
    }
}
