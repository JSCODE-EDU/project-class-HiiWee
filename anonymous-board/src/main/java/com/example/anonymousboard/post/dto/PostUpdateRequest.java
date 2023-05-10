package com.example.anonymousboard.post.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostUpdateRequest {

    @NotBlank(message = "제목이 없습니다.")
    private String title;

    @NotBlank(message = "내용이 없습니다.")
    private String content;

    public PostUpdateRequest() {
    }

    @Builder
    private PostUpdateRequest(final String title, final String content) {
        this.title = title;
        this.content = content;
    }
}
