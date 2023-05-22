package com.example.anonymousboard.member.repository;

import com.example.anonymousboard.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

   boolean existsByEmailValue(String email);
}
