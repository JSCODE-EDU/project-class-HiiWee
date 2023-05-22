package com.example.anonymousboard.member.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;

import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.dto.SignUpRequest;
import com.example.anonymousboard.member.exception.DuplicateEmailException;
import com.example.anonymousboard.member.exception.InvalidPasswordConfirmationException;
import com.example.anonymousboard.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    Member newMember;

    @BeforeEach
    void setUp() {
        newMember = Member.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .build();
    }

    @DisplayName("회원가입을 한다.")
    @Test
    void signUp() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .passwordConfirmation("!qwer123")
                .build();
        given(memberRepository.save(any())).willReturn(newMember);

        // when & then
        assertThatNoException()
                .isThrownBy(() -> memberService.signUp(signUpRequest));
    }

    @DisplayName("비밀번호와 비밀번호 확인이 다르면 회원가입에 실패한다.")
    @Test
    void signUp_exception_invalidPasswordConfirmation() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .passwordConfirmation("!qwerqwer123")
                .build();

        // when & then
        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(InvalidPasswordConfirmationException.class)
                .hasMessageContaining("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    @DisplayName("이미 존재하는 이메일이라면 회원가입에 실패한다.")
    @Test
    void signUp_exception_duplicateEmail() {
        // given
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .email("duplicate@mail.com")
                .password("!qwer123")
                .passwordConfirmation("!qwer123")
                .build();
        given(memberRepository.existsByEmailValue(any())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> memberService.signUp(signUpRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("이미 사용중인 이메일 입니다.");
    }

}