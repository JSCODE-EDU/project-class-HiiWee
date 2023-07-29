package com.example.anonymousboard.comment.dto;

import com.example.anonymousboard.comment.domain.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {

    private String email;
    private String content;
    private LocalDateTime createdAt;

    private CommentResponse() {
    }

    @Builder
    private CommentResponse(final String content, final LocalDateTime createdAt, final String email) {
        this.content = content;
        this.createdAt = createdAt;
        this.email = email;
    }

    public static CommentResponse from(final Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .email(comment.getWriterEmail())
                .build();
    }
}
