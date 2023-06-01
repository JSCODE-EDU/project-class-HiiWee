package com.example.anonymousboard.like.dto;

import lombok.Getter;

@Getter
public class PostLikeFlipResponse {

    private boolean like;

    private PostLikeFlipResponse() {
    }

    public PostLikeFlipResponse(final boolean like) {
        this.like = like;
    }
}
