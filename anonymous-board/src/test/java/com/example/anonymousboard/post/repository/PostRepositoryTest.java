package com.example.anonymousboard.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.anonymousboard.config.JpaConfig;
import com.example.anonymousboard.post.domain.Content;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.domain.Title;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@DataJpaTest
@Import(JpaConfig.class)
class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;

    Post post1;
    Post post2;
    Post post3;
    Post post4;

    @BeforeEach
    void setUp() {
        post1 = Post.builder()
                .title(Title.from("제목1"))
                .content(Content.from("내용1"))
                .build();
        post2 = Post.builder()
                .title(Title.from("제목2"))
                .content(Content.from("내용2"))
                .build();
        post3 = Post.builder()
                .title(Title.from("제목3"))
                .content(Content.from("내용3"))
                .build();
        post4 = Post.builder()
                .title(Title.from("제목4"))
                .content(Content.from("내용4"))
                .build();

        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

    }

    @DisplayName("게시글 개수는 임의로 정해 조회할 수 있다.")
    @Test
    void findPosts_with_limit() {
        // given
        Pageable pageable = PageRequest.of(0, 3, Direction.DESC, "createdAt");

        // when
        List<Post> posts = postRepository.findPostsByOrderByCreatedAtDesc(pageable).getContent();

        // then
        assertThat(posts.size()).isEqualTo(3);
    }

    @DisplayName("게시글은 생성일 기준으로 정렬하여 가져올 수 있다.")
    @Test
    void findPosts_with_createdAtDesc() {
        // given
        Pageable pageable = PageRequest.of(0, 4, Direction.DESC, "createdAt");

        // when
        List<Post> posts = postRepository.findPostsByOrderByCreatedAtDesc(pageable).getContent();

        // then
        assertThat(posts).containsExactly(post4, post3, post2, post1);
    }
}