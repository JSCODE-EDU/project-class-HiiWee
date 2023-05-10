package com.example.anonymousboard.post.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSaveRequest {

    @NotBlank(message = "제목을 반드시 입력해야 합니다.")
    private String title;

    private String content;

    private PostSaveRequest() {
    }

    @Builder
    private PostSaveRequest(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
