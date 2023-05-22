package com.example.anonymousboard.member.service;

import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.dto.SignUpRequest;
import com.example.anonymousboard.member.exception.DuplicateEmailException;
import com.example.anonymousboard.member.exception.InvalidPasswordConfirmationException;
import com.example.anonymousboard.member.repository.MemberRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void signUp(final SignUpRequest signUpRequest) {
        validate(signUpRequest);
        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .build();
        memberRepository.save(member);
    }

    private void validate(final SignUpRequest signUpRequest) {
        confirmPassword(signUpRequest.getPassword(), signUpRequest.getPasswordConfirmation());
        validateUniqueEmail(signUpRequest.getEmail());
    }

    private void validateUniqueEmail(final String email) {
        if (memberRepository.existsByEmailValue(email)) {
            throw new DuplicateEmailException();
        }
    }

    private void confirmPassword(final String password, final String passwordConfirmation) {
        if (!Objects.equals(password, passwordConfirmation)) {
            throw new InvalidPasswordConfirmationException();
        }
    }
}
