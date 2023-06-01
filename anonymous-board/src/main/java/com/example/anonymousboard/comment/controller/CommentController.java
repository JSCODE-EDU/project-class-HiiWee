package com.example.anonymousboard.comment.controller;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.comment.dto.CommentSaveRequest;
import com.example.anonymousboard.comment.dto.CommentSaveResponse;
import com.example.anonymousboard.comment.service.CommentService;
import com.example.anonymousboard.support.token.Login;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(final CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentSaveResponse> addComment(@Login final AuthInfo authInfo,
                                                          @PathVariable final Long postId,
                                                          @Valid @RequestBody CommentSaveRequest commentSaveRequest) {
        CommentSaveResponse commentSaveResponse = commentService.addComment(authInfo, postId, commentSaveRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentSaveResponse);
    }
}
