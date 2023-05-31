package com.example.anonymousboard.post.dto;

import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.comment.dto.CommentResponse;
import com.example.anonymousboard.post.domain.Post;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDetailResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final List<CommentResponse> comments;

    @Builder
    private PostDetailResponse(final Long id, final String title, final String content, final LocalDateTime createdAt, final List<CommentResponse> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.comments = comments;
    }

    public static PostDetailResponse of(final Post post, final List<Comment> comments) {
        List<CommentResponse> commentResponses = comments.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList());
        return PostDetailResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .comments(commentResponses)
                .build();
    }
}
