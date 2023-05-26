package com.example.anonymousboard.member.controller;

import static com.example.anonymousboard.util.ApiDocumentUtils.getDocumentRequest;
import static com.example.anonymousboard.util.ApiDocumentUtils.getDocumentResponse;
import static com.example.anonymousboard.util.DocumentFormatGenerator.getConstraints;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.advice.CommonErrorCode;
import com.example.anonymousboard.member.dto.MyInfoResponse;
import com.example.anonymousboard.member.dto.SignUpRequest;
import com.example.anonymousboard.member.exception.DuplicateEmailException;
import com.example.anonymousboard.member.exception.InvalidEmailFormatException;
import com.example.anonymousboard.member.exception.InvalidPasswordConfirmationException;
import com.example.anonymousboard.member.exception.InvalidPasswordFormatException;
import com.example.anonymousboard.member.exception.MemberErrorCode;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.member.service.MemberService;
import com.example.anonymousboard.support.AuthInterceptor;
import com.example.anonymousboard.support.token.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(MemberController.class)
@ExtendWith(RestDocumentationExtension.class)
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberService memberService;

    @MockBean
    AuthInterceptor authInterceptor;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    SignUpRequest signUpRequest;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
        given(authInterceptor.preHandle(any(), any(), any()))
                .willReturn(true);

        signUpRequest = SignUpRequest.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .passwordConfirmation("!qwer123")
                .build();
    }

    @DisplayName("회원가입을 하면 201을 반환한다.")
    @Test
    void signUp() throws Exception {
        // given
        doNothing().when(memberService).signUp(any());

        // when
        ResultActions result = mockMvc.perform(post("/members/signup")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isCreated()
        ).andDo(
                document("member/signUp/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일")
                                        .attributes(getConstraints("constraints",
                                                "이메일은 공백이 있으면 안되며, @이 반드시 1개 존재해야 합니다.")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                        .attributes(
                                                getConstraints("constraints",
                                                        "패스워드는 공백 허용 x, 최소 8자이상 ~ 15자이하, 하나 이상의 영문, 숫자, 특수 문자(@$!%*#?&)를 포함")),
                                fieldWithPath("passwordConfirmation").type(JsonFieldType.STRING).description("비밀번호 확인")
                                        .attributes(getConstraints("constraints", "패스워드와 동일 해야 한다."))
                        )
                )
        );
    }

    @DisplayName("이미 가입한 이메일로 회원가입을 하면 400을 반환한다.")
    @Test
    void signUp_exception_duplicateEmail() throws Exception {
        // given
        doThrow(new DuplicateEmailException()).when(memberService)
                .signUp(any());

        // when
        ResultActions result = mockMvc.perform(post("/members/signup")
                .content(objectMapper.writeValueAsString(signUpRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isBadRequest(),
                jsonPath("$.message").value("이미 사용중인 이메일 입니다."),
                jsonPath("$.errorCode").value(MemberErrorCode.DUPLICATE_EMAIL.value())
        ).andDo(
                document("member/signUp/fail/duplicatedEmail",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일")
                                        .attributes(getConstraints("constraints",
                                                "이메일은 공백이 있으면 안되며, @이 반드시 1개 존재해야 합니다.")),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                        .attributes(
                                                getConstraints("constraints",
                                                        "패스워드는 공백 허용 x, 최소 8자이상 ~ 15자이하, 하나 이상의 영문, 숫자, 특수 문자(@$!%*#?&)를 포함")),
                                fieldWithPath("passwordConfirmation").type(JsonFieldType.STRING).description("비밀번호 확인")
                                        .attributes(getConstraints("constraints", "패스워드와 동일 해야 한다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("이메일 포멧이 맞지 않다면 400을 반환한다.")
    @Test
    void signUp_exception_invalidEmailFormat() throws Exception {
        // given
        SignUpRequest invalidEmailRequest = SignUpRequest.builder()
                .email("invalidemail.com")
                .password("!qwer123")
                .passwordConfirmation("!qwer123")
                .build();
        doThrow(new InvalidEmailFormatException()).when(memberService)
                .signUp(any());

        // when
        ResultActions result = mockMvc.perform(post("/members/signup")
                .content(objectMapper.writeValueAsString(invalidEmailRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isBadRequest(),
                jsonPath("$.message").value("잘못된 이메일 형식입니다! (이메일 아이디는 영어로 시작해야 하며, 하나의 '@'를 포함하고 있어야 합니다."),
                jsonPath("$.errorCode").value(MemberErrorCode.INVALID_EMAIL_FORMAT.value())
        ).andDo(
                document("member/signUp/fail/invalidEmailFormat",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("비밀번호와 비밀번호 확인이 다르다면 400을 반환한다.")
    @Test
    void signUp_exception_invalidPasswordConfirmation() throws Exception {
        // given
        SignUpRequest invalidPasswordConfirmationRequest = SignUpRequest.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .passwordConfirmation("qwer!qwer123")
                .build();
        doThrow(new InvalidPasswordConfirmationException()).when(memberService)
                .signUp(any());

        // when
        ResultActions result = mockMvc.perform(post("/members/signup")
                .content(objectMapper.writeValueAsString(invalidPasswordConfirmationRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isBadRequest(),
                jsonPath("$.message").value("비밀번호와 비밀번호 확인이 일치하지 않습니다."),
                jsonPath("$.errorCode").value(MemberErrorCode.INVALID_PASSWORD_CONFIRMATION.value())
        ).andDo(
                document("member/signUp/fail/invalidPasswordConfirmation",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("비밀번호 포멧이 맞지 않다면 400을 반환한다.")
    @Test
    void signUp_exception_invalidPasswordFormat() throws Exception {
        // given
        SignUpRequest invalidPasswordFormatRequest = SignUpRequest.builder()
                .email("valid@mail.com")
                .password("qwe123")
                .passwordConfirmation("qwe123")
                .build();
        doThrow(new InvalidPasswordFormatException()).when(memberService)
                .signUp(any());

        // when
        ResultActions result = mockMvc.perform(post("/members/signup")
                .content(objectMapper.writeValueAsString(invalidPasswordFormatRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isBadRequest(),
                jsonPath("$.message").value("패스워드는 최소 8자이상 ~ 15자이하 까지 이며 하나 이상의 영문, 숫자, 특수 문자(@$!%*#?&)를 포함해야 합니다."),
                jsonPath("$.errorCode").value(MemberErrorCode.INVALID_PASSWORD_FORMAT.value())
        ).andDo(
                document("member/signUp/fail/invalidPasswordFormat",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("이메일, 패스워드 중 하나라도 비어있는 값인 경우 400을 반환한다.")
    @Test
    void signUp_exception_emptyRequest() throws Exception {
        // given
        SignUpRequest emptyRequest = SignUpRequest.builder()
                .email("")
                .password("!qwer123")
                .passwordConfirmation("!qwer123")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/members/signup")
                .content(objectMapper.writeValueAsString(emptyRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isBadRequest(),
                jsonPath("$.message").value("이메일은 반드시 입력해야 합니다."),
                jsonPath("$.errorCode").value(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value())
        ).andDo(
                document("member/signUp/fail/emptyRequest",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("내 정보를 조회하면 200을 반환한다.")
    @Test
    void findMyInfo() throws Exception {
        // given
        MyInfoResponse myInfoResponse = MyInfoResponse.builder()
                .id(1L)
                .email("valid@main.com")
                .createdAt(LocalDateTime.now())
                .build();
        given(memberService.findMyInfo(any())).willReturn(myInfoResponse);

        // when
        ResultActions result = mockMvc.perform(get("/members/me")
                .header("Authorization", "any"));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(1L),
                jsonPath("$.email").value("valid@main.com"),
                jsonPath("$.createdAt").isNotEmpty()
        ).andDo(
                document("member/findMyInfo/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("내 고유 회원 id"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("내 이메일"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("내 가입일자")
                        )
                )
        );
    }

    @DisplayName("없는 id를 통해 내 정보 조회시 404를 반환")
    @Test
    void findMyInfo_exception_notFoundId() throws Exception {
        // given
        given(memberService.findMyInfo(any())).willThrow(new MemberNotFoundException());

        // when
        ResultActions result = mockMvc.perform(get("/members/me")
                .header("Authorization", "any"));

        // then
        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.errorCode").value(MemberErrorCode.MEMBER_NOT_FOUND.value()),
                jsonPath("$.message").value("회원을 찾을 수 없습니다.")
        ).andDo(
                document("member/findMyInfo/fail/notFountId",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }
}