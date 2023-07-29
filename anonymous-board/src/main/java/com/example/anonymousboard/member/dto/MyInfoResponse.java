package com.example.anonymousboard.member.dto;

import com.example.anonymousboard.member.domain.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MyInfoResponse {

    private Long id;
    private String email;
    private LocalDateTime createdAt;

    private MyInfoResponse() {
    }

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
