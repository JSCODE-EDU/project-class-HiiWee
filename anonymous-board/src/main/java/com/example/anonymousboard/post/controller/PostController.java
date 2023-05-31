package com.example.anonymousboard.post.controller;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostDetailResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.service.PostService;
import com.example.anonymousboard.support.token.Login;
import java.util.Objects;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostSaveResponse> createPost(@Login final AuthInfo authInfo,
                                                       @Valid @RequestBody final PostSaveRequest postSaveRequest) {
        PostSaveResponse saveResponse = postService.createPost(authInfo, postSaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveResponse);
    }

    @GetMapping("/posts")
    public ResponseEntity<PagePostsResponse> findPosts(
            @Nullable @RequestParam final String keyword,
            @PageableDefault(sort = "createdAt", direction = Direction.DESC, size = 100) final Pageable pageable) {
        if (Objects.isNull(keyword)) {
            PagePostsResponse findPosts = postService.findPosts(pageable);
            return ResponseEntity.ok(findPosts);
        }
        PagePostsResponse findPosts = postService.findPostsByKeyword(keyword, pageable);
        return ResponseEntity.ok(findPosts);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDetailResponse> findPost(@PathVariable final Long postId) {
        PostDetailResponse findPost = postService.findPostDetailById(postId);
        return ResponseEntity.ok(findPost);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponse> updatePost(@Login final AuthInfo authInfo,
                                                   @PathVariable final Long postId,
                                                   @Valid @RequestBody final PostUpdateRequest postUpdateRequest) {
        postService.updatePostById(authInfo, postId, postUpdateRequest);
        PostResponse updatedPost = postService.findPostById(postId);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@Login final AuthInfo authInfo,
                                           @PathVariable final Long postId) {
        postService.deletePostById(authInfo, postId);
        return ResponseEntity.noContent().build();
    }
}
