package com.example.anonymousboard.util.fixture;

import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPost;

import com.example.anonymousboard.auth.dto.LoginRequest;
import com.example.anonymousboard.auth.dto.TokenResponse;

public class TokenFixture {

    private static final String AUTHORIZATION_PREFIX = "Bearer ";

    public static String getMemberToken() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("valid01@mail.com")
                .password("!qwer123")
                .build();
        return AUTHORIZATION_PREFIX + httpPost(loginRequest, "/login")
                .jsonPath()
                .getObject(".", TokenResponse.class)
                .getToken();
    }

    public static String getOtherMemberToken() {
        LoginRequest loginRequest = LoginRequest.builder()
                .email("valid02@mail.com")
                .password("!qwer123")
                .build();
        return AUTHORIZATION_PREFIX + httpPost(loginRequest, "/login")
                .jsonPath()
                .getObject(".", TokenResponse.class)
                .getToken();
    }
}
