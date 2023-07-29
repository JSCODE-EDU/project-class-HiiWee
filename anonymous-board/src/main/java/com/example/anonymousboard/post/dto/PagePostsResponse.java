package com.example.anonymousboard.post.dto;

import com.example.anonymousboard.post.domain.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PagePostsResponse {

    private List<PostResponse> postResponses;
    private long totalPostCount;

    private PagePostsResponse() {
    }

    @Builder
    private PagePostsResponse(final List<PostResponse> postResponses, final long totalPostCount) {
        this.postResponses = postResponses;
        this.totalPostCount = totalPostCount;
    }

    public static PagePostsResponse from(final List<Post> posts) {
        List<PostResponse> postResponses = posts.stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
        return PagePostsResponse.builder()
                .postResponses(postResponses)
                .totalPostCount(posts.size())
                .build();
    }
}
