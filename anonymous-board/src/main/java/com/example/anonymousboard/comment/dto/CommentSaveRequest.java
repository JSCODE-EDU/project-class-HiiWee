package com.example.anonymousboard.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentSaveRequest {

    @NotBlank
    private String content;

    private CommentSaveRequest() {
    }

    public CommentSaveRequest(final String content) {
        this.content = content;
    }
}
