package com.example.anonymousboard.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.anonymousboard.post.domain.Content;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.domain.Title;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    Post post;

    @BeforeEach
    void setUp() {
        post = Post.builder()
                .id(1L)
                .content(Content.from("안녕하세요!"))
                .title(Title.from("첫 게시글 입니다!"))
                .build();
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void createProduct_success() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(post);
        PostSaveRequest saveRequest = PostSaveRequest.builder()
                .title("안녕하세요!")
                .content("첫 게시글 입니다!")
                .build();

        // when
        Long savedId = postService.createPost(saveRequest);

        // then
        assertThat(savedId).isEqualTo(1L);
    }


}