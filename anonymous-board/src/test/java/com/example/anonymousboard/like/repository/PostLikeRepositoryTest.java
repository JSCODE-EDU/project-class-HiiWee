package com.example.anonymousboard.like.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.anonymousboard.like.domain.PostLike;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.util.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostLikeRepositoryTest extends RepositoryTest {

    Member member;

    Member otherMember;

    Post post;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("valid1@mail.com")
                .password("!qwer123")
                .build();
        otherMember = Member.builder()
                .email("valid2@mail.com")
                .password("!qwer123")
                .build();
        post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();

        memberRepository.save(member);
        memberRepository.save(otherMember);
        postRepository.save(post);
    }

    @DisplayName("이미 좋아요를 누른 게시글이라면 true를 반환한다.")
    @Test
    void existsByPostAndMemberId() {
        // given
        PostLike postLike = PostLike.builder()
                .post(post)
                .member(otherMember)
                .build();
        postLikeRepository.save(postLike);

        // when
        boolean result = postLikeRepository.existsByPostAndMemberId(post, otherMember.getId());

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("좋아요를 취소한다.")
    @Test
    void deleteByPostAndMemberId() {
        // given
        PostLike postLike = PostLike.builder()
                .post(post)
                .member(otherMember)
                .build();
        postLikeRepository.save(postLike);

        // when
        postLikeRepository.deleteByPostAndMemberId(post, otherMember.getId());
        boolean result = postLikeRepository.existsByPostAndMemberId(post, otherMember.getId());

        // then
        assertThat(result).isFalse();
    }
}