package com.example.anonymousboard.comment.dto;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentSaveRequest {

    @NotBlank(message = "댓글을 반드시 입력해야 합니다.")
    private String content;

    private CommentSaveRequest() {
    }

    public CommentSaveRequest(final String content) {
        this.content = content;
    }
}
