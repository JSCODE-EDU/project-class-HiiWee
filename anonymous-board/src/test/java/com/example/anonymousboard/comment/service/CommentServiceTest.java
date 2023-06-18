package com.example.anonymousboard.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.comment.dto.CommentSaveRequest;
import com.example.anonymousboard.comment.dto.CommentSaveResponse;
import com.example.anonymousboard.member.domain.Encryptor;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.domain.Password;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.util.ServiceTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentServiceTest extends ServiceTest {

    Member member;

    Post post;

    Comment comment;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("valid@mail.com")
                .password(Password.of(encryptor, "!qwer123"))
                .build();
        post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(member)
                .build();
        comment = new Comment(1L, "댓글입니다.", member, post);
    }

    @DisplayName("댓글을 작성할 수 있다.")
    @Test
    void addComment() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post));
        given(commentRepository.save(any())).willReturn(comment);
        AuthInfo authInfo = new AuthInfo(1L);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("댓글입니다.");

        // when
        CommentSaveResponse commentSaveResponse = commentService.addComment(authInfo, 1L, commentSaveRequest);

        // then
        assertThat(commentSaveResponse.getSavedId()).isEqualTo(1L);
    }

    @DisplayName("댓글을 작성하는 사용자가 없으면 댓글을 작성할 수 없다.")
    @Test
    void addComment_exception_notFoundMember() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());
        AuthInfo authInfo = new AuthInfo(1L);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("댓글입니다.");

        // when & then
        assertThatThrownBy(() -> commentService.addComment(authInfo, 1L, commentSaveRequest))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @DisplayName("댓글을 작성하려는 게시글이 없다면 댓글을 작성할 수 없다.")
    @Test
    void addComment_exception_notFoundPost() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));
        given(postRepository.findById(any())).willReturn(Optional.empty());
        AuthInfo authInfo = new AuthInfo(1L);
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("댓글입니다.");

        // when & then
        assertThatThrownBy(() -> commentService.addComment(authInfo, 1L, commentSaveRequest))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }
}