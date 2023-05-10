package com.example.anonymousboard.post.controller;

import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.service.PostService;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostSaveResponse> createPost(@Valid @RequestBody final PostSaveRequest postSaveRequest) {
        Long savedId = postService.createPost(postSaveRequest);
        return ResponseEntity.created(URI.create("/posts")).body(PostSaveResponse.createPostSuccess(savedId));
    }

    @GetMapping("/posts")
    public ResponseEntity<PagePostsResponse> findPosts(
            @PageableDefault(sort = "createdAt", direction = Direction.DESC, size = 100) final Pageable pageable) {
        PagePostsResponse findPosts = postService.findPosts(pageable);
        return ResponseEntity.ok(findPosts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> findPost(@PathVariable Long postId) {
        PostResponse findPost = postService.findPostById(postId);
        return ResponseEntity.ok(findPost);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long postId,
                                                   @Valid @RequestBody PostUpdateRequest postUpdateRequest) {
        PostResponse updatePost = postService.updatePostById(postId, postUpdateRequest);
        return ResponseEntity.ok(updatePost);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePostById(postId);
        return ResponseEntity.noContent().build();
    }
}
