package com.example.anonymousboard.post.controller;

import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.service.PostService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(PostSaveResponse.createPostSuccess(savedId));
    }
}
