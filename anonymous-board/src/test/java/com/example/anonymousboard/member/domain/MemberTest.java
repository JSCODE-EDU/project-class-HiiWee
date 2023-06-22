package com.example.anonymousboard.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.member.exception.InvalidEmailFormatException;
import com.example.anonymousboard.member.exception.InvalidPasswordFormatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    Encryptor encryptor;

    @BeforeEach
    void setUp() {
        encryptor = new Encryptor();
    }

    @DisplayName("회원을 생성할 수 있다.")
    @Test
    void createMember() {
        // given
        String password = "!qwer1234";
        String email = "test123@test.com";

        // when
        Member member = Member.builder()
                .email(email)
                .password(Password.of(encryptor, password))
                .build();

        // then
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(email),
                () -> assertThat(member.getPassword()).isEqualTo(encryptor.encrypt(password))
        );
    }

    @DisplayName("잘못된 이메일 형식이 입력되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"invalidemail.com", " valid@email.com", "123asdf@naver.com"})
    void createMember_exception_invalidEmailFormat(String invalidEmail) {
        // given
        String password = "!qwer1234";

        // when & then
        assertThatThrownBy(
                () -> Member.builder().email(invalidEmail).password(Password.of(encryptor, password)).build())
                .isInstanceOf(InvalidEmailFormatException.class)
                .hasMessageContaining("잘못된 이메일 형식입니다! (이메일 아이디는 영어로 시작해야 하며, 하나의 '@'를 포함하고 있어야 합니다.");
    }

    @DisplayName("잘못된 패스워드 형식이 입력되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"!qwer12", "!qwerqwerqwer123", " !qwer123", "qwer123", "!123123"})
    void createMember_exception_invalidPasswordFormat(String invalidPassword) {
        // given
        String email = "valid123@mail.com";

        // when & then
        assertThatThrownBy(
                () -> Member.builder().email(email).password(Password.of(encryptor, invalidPassword)).build())
                .isInstanceOf(InvalidPasswordFormatException.class)
                .hasMessageContaining("패스워드는 최소 8자이상 ~ 15자이하 까지 이며 하나 이상의 영문, 숫자, 특수 문자(@$!%*#?&)를 포함해야 합니다.");
    }
}