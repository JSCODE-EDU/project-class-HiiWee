package com.example.anonymousboard.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.anonymousboard.auth.dto.LoginRequest;
import com.example.anonymousboard.auth.dto.TokenResponse;
import com.example.anonymousboard.auth.exception.LoginFailedException;
import com.example.anonymousboard.member.domain.Encryptor;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.domain.Password;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.util.DatabaseCleaner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    Encryptor encryptor;

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DatabaseCleaner databaseCleaner;

    Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("test123@test.com")
                .password(Password.of(encryptor, "!qwer123"))
                .build();

        memberRepository.save(member);
    }

    @AfterEach
    void clearDatabase() {
        databaseCleaner.clear();
    }

    @DisplayName("로그인 요청이 들어오면 토큰을 생성한다.")
    @Test
    void createToken() {
        // given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("test123@test.com")
                .password("!qwer123")
                .build();

        // when
        TokenResponse tokenResponse = authService.createToken(loginRequest);

        // then
        assertThat(tokenResponse.getToken()).isNotNull();
    }

    @DisplayName("잘못된 정보로 로그인 요청이 들어오면 토큰 생성을 실패한다.")
    @Test
    void createToken_exception_invalidLoginRequest() {
        // given
        LoginRequest loginRequest = LoginRequest.builder()
                .email("invalid@test.com")
                .password("!qwer123")
                .build();

        // when & then
        assertThatThrownBy(() -> authService.createToken(loginRequest))
                .isInstanceOf(LoginFailedException.class)
                .hasMessageContaining("아이디 혹은 비밀번호가 잘못되었습니다.");
    }
}