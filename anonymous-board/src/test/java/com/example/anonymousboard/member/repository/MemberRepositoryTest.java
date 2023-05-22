package com.example.anonymousboard.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.anonymousboard.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("동일한 이메일이 저장되어 있다면 true를 반환한다.")
    @Test
    void existsByEmailValue() {
        // given
        Member member = Member.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .build();

        // when
        memberRepository.save(member);

        // then
        assertThat(memberRepository.existsByEmailValue("valid@mail.com")).isTrue();
    }

}