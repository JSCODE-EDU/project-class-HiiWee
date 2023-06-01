package com.example.anonymousboard.like.controller;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.like.dto.PostLikeFlipResponse;
import com.example.anonymousboard.like.service.PostLikeService;
import com.example.anonymousboard.support.token.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(final PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<PostLikeFlipResponse> likePost(@Login AuthInfo authInfo,
                                                         @PathVariable Long postId) {
        PostLikeFlipResponse postLikeFlipResponse = postLikeService.flipPostLike(authInfo, postId);
        return ResponseEntity.ok(postLikeFlipResponse);
    }
}
