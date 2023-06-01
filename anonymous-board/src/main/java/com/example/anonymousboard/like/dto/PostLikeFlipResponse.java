package com.example.anonymousboard.like.dto;

import lombok.Getter;

@Getter
public class PostLikeFlipResponse {

    private boolean isLiked;

    private PostLikeFlipResponse() {
    }

    public PostLikeFlipResponse(final boolean isLiked) {
        this.isLiked = isLiked;
    }
}
