package com.example.anonymousboard.comment.dto;

import lombok.Getter;

@Getter
public class CommentSaveResponse {

    private Long savedId;

    private CommentSaveResponse() {
    }

    public CommentSaveResponse(final Long savedId) {
        this.savedId = savedId;
    }
}
