package com.example.anonymousboard.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PagePostsDetailResponse {

    private final List<PostDetailResponse> posts;
    private final long totalPostCount;

    @Builder
    private PagePostsDetailResponse(final List<PostDetailResponse> posts, final long totalPostCount) {
        this.posts = posts;
        this.totalPostCount = totalPostCount;
    }

    public static PagePostsDetailResponse from(final List<PostDetailResponse> posts) {
        return PagePostsDetailResponse.builder()
                .posts(posts)
                .totalPostCount(posts.size())
                .build();
    }


}
