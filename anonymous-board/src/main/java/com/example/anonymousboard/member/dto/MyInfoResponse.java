package com.example.anonymousboard.member.dto;

import com.example.anonymousboard.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyInfoResponse {

    private final Long id;
    private final String email;
    private final LocalDateTime createdAt;

    @Builder
    private MyInfoResponse(final Long id, final String email, final LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static MyInfoResponse from(final Member member) {
        return MyInfoResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .createdAt(member.getCreatedAt())
                .build();
    }
}
