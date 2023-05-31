package com.example.anonymousboard.comment.service;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.comment.dto.CommentSaveRequest;
import com.example.anonymousboard.comment.dto.CommentSaveResponse;
import com.example.anonymousboard.comment.repository.CommentRepository;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CommentService(final CommentRepository commentRepository, final MemberRepository memberRepository,
                          final PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    public CommentSaveResponse addComment(final AuthInfo authInfo, final Long postId,
                                          final CommentSaveRequest commentSaveRequest) {
        Member member = findMember(authInfo.getId());
        Post post = findPost(postId);
        Comment comment = Comment.builder()
                .content(commentSaveRequest.getContent())
                .member(member)
                .post(post)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return new CommentSaveResponse(savedComment.getId());
    }

    private Post findPost(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Member findMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}
