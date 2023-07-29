package com.example.anonymousboard.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PagePostsDetailResponse {

    private List<PostDetailResponse> posts;
    private long totalPostCount;

    private PagePostsDetailResponse() {
    }

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
