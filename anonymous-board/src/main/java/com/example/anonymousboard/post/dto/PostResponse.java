package com.example.anonymousboard.post.dto;

import com.example.anonymousboard.post.domain.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private PostResponse() {
    }

    @Builder
    private PostResponse(final Long id, final String title, final String content, final LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static PostResponse from(final Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .build();
    }
}
