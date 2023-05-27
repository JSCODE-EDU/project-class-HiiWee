package com.example.anonymousboard.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.anonymousboard.comment.exception.InvalidCommentException;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.post.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CommentTest {

    @DisplayName("댓글을 생성할 수 있다.")
    @Test
    void createComment() {
        // given
        Post post = Post.builder()
                .title("제목.")
                .content("내용.")
                .build();
        Member member = Member.builder()
                .email("valid@main.com")
                .password("!qwer123")
                .build();
        String content = "첫 댓글 입니다!";

        // when
        Comment comment = Comment.builder()
                .content(content)
                .member(member)
                .post(post)
                .build();

        assertThat(comment.getContent()).isEqualTo("첫 댓글 입니다!");
    }

    @DisplayName("댓글 내용이 1 ~ 50자가 아니라면 댓글을 생성할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"  ", " asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfa "})
    void createComment_exception_invalidContent(String invalidContent) {

        // when & then
        assertThatThrownBy(() -> Comment.builder().content(invalidContent).build())
                .isInstanceOf(InvalidCommentException.class)
                .hasMessageContaining("댓글은 1자 이상 50자 이하까지 작성할 수 있습니다");
    }
}