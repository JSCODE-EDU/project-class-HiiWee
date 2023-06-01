package com.example.anonymousboard.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.like.dto.PostLikeFlipResponse;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.util.ServiceTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostLikeServiceTest extends ServiceTest {

    Member member;

    Member otherMember;

    Post post;


    @BeforeEach
    void setUp() {
        otherMember = Member.builder()
                .email("valid2@mail.com")
                .password("!qwer123")
                .build();
        member = Member.builder()
                .email("valid1@mail.com")
                .password("!qwer123")
                .build();
        post = Post.builder()
                .title("제목")
                .content("내용")
                .member(member)
                .build();
    }

    @DisplayName("좋아요를 누를 수 있다.")
    @Test
    void flipPostLike() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(postLikeRepository.existsByPostAndMemberId(any(), any())).willReturn(false);
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(otherMember));
        AuthInfo authInfo = new AuthInfo(1L);

        // when
        PostLikeFlipResponse postLikeFlipResponse = postLikeService.flipPostLike(authInfo, 1L);

        // then
        assertThat(postLikeFlipResponse.isLike()).isTrue();
    }

    @DisplayName("좋아요를 취소할 수 있다.")
    @Test
    void cancelFlipPostLike() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(postLikeRepository.existsByPostAndMemberId(any(), any())).willReturn(true);
        AuthInfo authInfo = new AuthInfo(1L);

        // when
        PostLikeFlipResponse postLikeFlipResponse = postLikeService.flipPostLike(authInfo, 1L);

        // then
        assertThat(postLikeFlipResponse.isLike()).isFalse();
    }

    @DisplayName("좋아요를 하려는 게시글이 없다면 예외가 발생한다.")
    @Test
    void flipPostLike_exception_postNotFound() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.empty());
        AuthInfo authInfo = new AuthInfo(1L);

        // when & then
        assertThatThrownBy(() -> postLikeService.flipPostLike(authInfo, 1L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @DisplayName("좋아요를 누르려는 사용자가 존재하지 않는다면 예외가 발생한다.")
    @Test
    void flipPostLike_exception_memberNotFound() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(postLikeRepository.existsByPostAndMemberId(any(), any())).willReturn(false);
        given(memberRepository.findById(any())).willReturn(Optional.empty());
        AuthInfo authInfo = new AuthInfo(1L);

        // when & then
        assertThatThrownBy(() -> postLikeService.flipPostLike(authInfo, 1L))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }
}