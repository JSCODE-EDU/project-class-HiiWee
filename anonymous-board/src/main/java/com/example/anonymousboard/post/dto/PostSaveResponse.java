package com.example.anonymousboard.post.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSaveResponse {

    private final Long savedId;
    private final String message;

    @Builder
    private PostSaveResponse(final Long savedId, final String message) {
        this.savedId = savedId;
        this.message = message;
    }

    public static PostSaveResponse createPostSuccess(final Long savedId) {
        return PostSaveResponse.builder()
                .savedId(savedId)
                .message("게시글 작성을 완료했습니다.")
                .build();
    }
}
