package com.example.anonymousboard.like.service;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.like.domain.PostLike;
import com.example.anonymousboard.like.dto.PostLikeFlipResponse;
import com.example.anonymousboard.like.repository.PostLikeRepository;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public PostLikeService(final PostLikeRepository postLikeRepository, final MemberRepository memberRepository,
                           final PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public PostLikeFlipResponse flipPostLike(final AuthInfo authInfo, final Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        Long memberId = authInfo.getId();
        if (postLikeRepository.existsByPostAndMemberId(post, memberId)) {
            postLikeRepository.deleteByPostAndMemberId(post, memberId);
            return new PostLikeFlipResponse(false);
        }
        addPostLike(post, memberId);
        return new PostLikeFlipResponse(true);
    }

    private void addPostLike(final Post post, final Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        PostLike postLike = PostLike.builder()
                .member(member)
                .post(post)
                .build();
        postLikeRepository.save(postLike);
    }
}
