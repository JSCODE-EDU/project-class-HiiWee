package com.example.anonymousboard.auth.controller;


import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.auth.dto.LoginRequest;
import com.example.anonymousboard.auth.dto.TokenResponse;
import com.example.anonymousboard.auth.exception.AuthErrorCode;
import com.example.anonymousboard.auth.exception.LoginFailedException;
import com.example.anonymousboard.util.controller.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class AuthControllerTest extends ControllerTest {

    LoginRequest loginRequest;

    TokenResponse tokenResponse;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        given(authInterceptor.preHandle(any(), any(), any()))
                .willReturn(true);

        loginRequest = LoginRequest.builder()
                .email("test@mail.com")
                .password("!qwer1234")
                .build();
        tokenResponse = TokenResponse.from("thisIsValidTokenForTest");
    }

    @DisplayName("로그인을 성공하면 200을 반환한다.")
    @Test
    void login() throws Exception {
        // given
        given(authService.createToken(any())).willReturn(tokenResponse);

        // when
        ResultActions result = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(loginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.token").value("thisIsValidTokenForTest")
        ).andDo(
                document("auth/login/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("token").type(JsonFieldType.STRING).description("생성된 JWT 토큰")
                        )
                )
        );
    }

    @DisplayName("잘못된 이메일 혹은 패스워드로 로그인을 시도하면 401을 반환한다.")
    @Test
    void login_exception_invalidEmailOrPassword() throws Exception {
        // given
        LoginRequest wrongLoginRequest = LoginRequest.builder()
                .email("wrong@mail.com")
                .password("!wrongPassword123")
                .build();
        given(authService.createToken(any())).willThrow(new LoginFailedException());

        // when
        ResultActions result = mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(wrongLoginRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isUnauthorized(),
                jsonPath("$.message").value("아이디 혹은 비밀번호가 잘못되었습니다."),
                jsonPath("$.errorCode").value(AuthErrorCode.LOGIN_FAILED.value())
        ).andDo(
                document("auth/login/fail/wrongEmailOrPassword",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("로그인 패스워드")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }
}