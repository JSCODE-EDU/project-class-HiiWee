package com.example.anonymousboard.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.member.exception.InvalidEmailFormatException;
import com.example.anonymousboard.member.exception.InvalidPasswordFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MemberTest {

    @DisplayName("회원을 생성할 수 있다.")
    @Test
    void createMember() {
        // given
        String password = "!qwer1234";
        String email = "test123@test.com";

        // when
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        // then
        assertAll(
                () -> assertThat(member.getEmailValue()).isEqualTo(email),
                () -> assertThat(member.getPasswordValue()).isEqualTo(password)
        );
    }

    @DisplayName("잘못된 이메일 형식이 입력되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"invalidemail.com", " valid@email.com", "123asdf@naver.com"})
    void createMember_exception_invalidEmailFormat(String invalidEmail) {
        // given
        String password = "!qwer1234";

        // when & then
        assertThatThrownBy(() -> Member.builder().email(invalidEmail).password(password).build())
                .isInstanceOf(InvalidEmailFormatException.class);
    }

    @DisplayName("잘못된 패스워드 형식이 입력되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"!qwer12", "!qwerqwerqwer123", " !qwer123", "qwer123", "!123123"})
    void createMember_exception_invalidPasswordFormat(String invalidPassword) {
        // given
        String email = "valid123@mail.com";

        // when & then
        assertThatThrownBy(() -> Member.builder().email(email).password(invalidPassword).build())
                .isInstanceOf(InvalidPasswordFormatException.class);
    }
}