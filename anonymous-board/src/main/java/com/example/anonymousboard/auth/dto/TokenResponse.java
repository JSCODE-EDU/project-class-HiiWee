package com.example.anonymousboard.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponse {

    private String token;

    private TokenResponse() {
    }

    @Builder
    private TokenResponse(final String token) {
        this.token = token;
    }

    public static TokenResponse from(final String token) {
        return new TokenResponse(token);
    }
}
