package com.example.anonymousboard.comment.dto;

import lombok.Getter;

@Getter
public class CommentSaveResponse {

    private final Long savedId;

    public CommentSaveResponse(final Long savedId) {
        this.savedId = savedId;
    }
}
