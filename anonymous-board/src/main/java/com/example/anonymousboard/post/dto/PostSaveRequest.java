package com.example.anonymousboard.post.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSaveRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private PostSaveRequest() {
    }

    @Builder
    private PostSaveRequest(final String title, final String content) {
        this.title = title;
        this.content = content;
    }

}
