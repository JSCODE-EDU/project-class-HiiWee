package com.example.anonymousboard.comment.repository;

import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.util.RepositoryTest;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CommentRepositoryTest extends RepositoryTest {

    Member member;

    Post post;

    Comment comment;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("valid@mail.com")
                .password("!qwer123")
                .build();
        memberRepository.save(member);

        post = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .member(member)
                .build();
        postRepository.save(post);

        comment = Comment.builder()
                .content("댓글입니다.")
                .member(member)
                .post(post)
                .build();
        commentRepository.save(comment);
    }

    @Test
    void findCommentsByPost() {
        // when
        List<Comment> comments = commentRepository.findCommentsByPost(post);

        // then
        assertAll(
                () -> Assertions.assertThat(comments.size()).isEqualTo(1),
                () -> Assertions.assertThat(comments.get(0).getContent()).isEqualTo("댓글입니다."),
                () -> Assertions.assertThat(comments.get(0).getWriterEmail()).isEqualTo("valid@mail.com")
        );
    }
}