package com.example.anonymousboard.auth.service;

import com.example.anonymousboard.auth.dto.LoginRequest;
import com.example.anonymousboard.auth.dto.TokenResponse;
import com.example.anonymousboard.auth.exception.LoginFailedException;
import com.example.anonymousboard.member.domain.Encryptor;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.support.token.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final Encryptor encryptor;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberRepository memberRepository,
                       final Encryptor encryptor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
        this.encryptor = encryptor;
    }

    public TokenResponse createToken(final LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailValueAndPasswordValue(loginRequest.getEmail(),
                encryptor.encrypt(loginRequest.getPassword())).orElseThrow(LoginFailedException::new);
        String accessToken = jwtTokenProvider.createAccessToken(member.getId());
        return TokenResponse.from(accessToken);
    }
}
