package com.example.anonymousboard.member.acceptance;

import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpGetWithAuthorization;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPost;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.advice.ErrorResponse;
import com.example.anonymousboard.auth.dto.LoginRequest;
import com.example.anonymousboard.auth.dto.TokenResponse;
import com.example.anonymousboard.member.dto.MyInfoResponse;
import com.example.anonymousboard.member.dto.SignUpRequest;
import com.example.anonymousboard.member.exception.MemberErrorCode;
import com.example.anonymousboard.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberAcceptanceTest extends AcceptanceTest {

    SignUpRequest signUpRequest;

    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = SignUpRequest.builder()
                .email("myvalidid01@valid.com")
                .password("!qwer1234")
                .passwordConfirmation("!qwer1234")
                .build();
        loginRequest = LoginRequest.builder()
                .email("myvalidid01@valid.com")
                .password("!qwer1234")
                .build();
    }

    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void signup() {
        // when
        ExtractableResponse<Response> response = httpPost(signUpRequest, "/members/signup");

        // then
        assertThat(response.statusCode()).isEqualTo(201);
    }

    @DisplayName("동일한 이메일로 회원가입을 할 수 없다.")
    @Test
    void signup_fail_sameEmail() {
        // given
        SignUpRequest sameEmailRequest = SignUpRequest.builder()
                .email("myvalidid01@valid.com")
                .password("!qwer1234")
                .passwordConfirmation("!qwer1234")
                .build();
        httpPost(signUpRequest, "/members/signup");

        // when
        ExtractableResponse<Response> response = httpPost(sameEmailRequest, "/members/signup");
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(400),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(MemberErrorCode.DUPLICATE_EMAIL.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("이미 사용중인 이메일 입니다.")
        );
    }

    @DisplayName("로그인을 하고 자기 자신의 프로필을 조회할 수 있다.")
    @Test
    void lookup_myProfile() {
        // given
        httpPost(signUpRequest, "/members/signup");
        ExtractableResponse<Response> loginResponse = httpPost(loginRequest, "/login");
        TokenResponse loginToken = loginResponse.jsonPath().getObject(".", TokenResponse.class);

        // when
        ExtractableResponse<Response> response = httpGetWithAuthorization("/members/me", "Bearer " + loginToken.getToken());
        MyInfoResponse myInfo = response.jsonPath().getObject(".", MyInfoResponse.class);

        // then
        assertAll(
                () -> assertThat(myInfo.getId()).isEqualTo(3L),
                () -> assertThat(myInfo.getEmail()).isEqualTo("myvalidid01@valid.com"),
                () -> assertThat(myInfo.getCreatedAt()).isNotNull()
        );
    }
}
