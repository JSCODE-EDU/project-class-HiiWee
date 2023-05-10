package com.example.anonymousboard.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;

    @Mock
    PostRepository postRepository;

    Post post1;
    Post post2;
    Post post3;
    Post post4;

    Page<Post> pagePosts;

    @BeforeEach
    void setUp() {
        post1 = Post.builder()
                .id(1L)
                .title("제목1")
                .content("내용1")
                .build();
        post2 = Post.builder()
                .id(2L)
                .title("제목2")
                .content("내용2")
                .build();
        post3 = Post.builder()
                .id(3L)
                .title("제목3")
                .content("내용3")
                .build();
        post4 = Post.builder()
                .id(4L)
                .title("제목4")
                .content("내용4")
                .build();

        pagePosts = new PageImpl<>(List.of(post4, post3, post2, post1));
    }


    @DisplayName("상품을 저장한다.")
    @Test
    void createProduct_success() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(post1);
        PostSaveRequest saveRequest = PostSaveRequest.builder()
                .title("제목1")
                .content("내용1")
                .build();

        // when
        Long savedId = postService.createPost(saveRequest);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @DisplayName("임의의 검색 조건을 통해 모든 게시글을 조회할 수 있다.")
    @Test
    void findPosts_with_limit100() {
        // given
        given(postRepository.findPostsByOrderByCreatedAtDesc(any())).willReturn(pagePosts);
        Pageable pageable = PageRequest.of(0, 4, Direction.DESC, "createdAt");

        // when
        PagePostsResponse posts = postService.findPosts(pageable);
        PostResponse postResponse = posts.getPostResponses().get(0);
        List<String> titles = posts.getPostResponses()
                .stream()
                .map(PostResponse::getTitle)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(posts.getTotalPostCount()).isEqualTo(4),
                () -> assertThat(titles).containsExactly("제목4", "제목3", "제목2", "제목1"),
                () -> assertThat(postResponse.getId()).isEqualTo(4L),
                () -> assertThat(postResponse.getTitle()).isEqualTo("제목4"),
                () -> assertThat(postResponse.getContent()).isEqualTo("내용4")
        );
    }

    @DisplayName("특정 id값의 게시글을 조회할 수 있다.")
    @Test
    void findPostById() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(post1));

        // when
        PostResponse findPost = postService.findPostById(1L);

        // then
        assertAll(
                () -> assertThat(findPost.getId()).isEqualTo(1L),
                () -> assertThat(findPost.getTitle()).isEqualTo("제목1"),
                () -> assertThat(findPost.getContent()).isEqualTo("내용1")
        );
    }

    @DisplayName("존재하지 않는 id의 게시글을 조회하면 예외가 발생한다.")
    @Test
    void findPostById_exception_notFoundPostId() {
        // given
        doThrow(new PostNotFoundException()).when(postRepository)
                .findById(any());

        // when & then
        assertThatThrownBy(() -> postService.findPostById(11111L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 게시글을 수정할 수 있다.")
    @Test
    void updatePost() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(post1));
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when
        PostResponse updatedPost = postService.updatePostById(1L, updateRequest);

        // then
        assertAll(
                () -> assertThat(updatedPost.getId()).isEqualTo(1L),
                () -> assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목"),
                () -> assertThat(updatedPost.getContent()).isEqualTo("수정된 내용"),
                () -> assertThat(updatedPost.getCreatedAt()).isEqualTo(post1.getCreatedAt())
        );
    }

    @DisplayName("존재하지 않는 게시글을 수정할 수 없다.")
    @Test
    void updatePost_exception_notFoundPostId() {
        // given
        doThrow(new PostNotFoundException()).when(postRepository)
                .findById(any());
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when & then
        assertThatThrownBy(() -> postService.updatePostById(1111L, updateRequest))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @DisplayName("특정 게시글을 삭제할 수 있다.")
    @Test
    void deletePost() {
        //given
        given(postRepository.findById(any())).willReturn(Optional.of(post1));
        doNothing().when(postRepository)
                .delete(any());

        assertThatNoException().isThrownBy(() -> postService.deletePostById(1L));
    }

    @DisplayName("존재하지 않는 게시글은 삭제할 수 없다.")
    @Test
    void deletePost_exception_notFoundPostId() {
        // given
        doThrow(new PostNotFoundException()).when(postRepository)
                .findById(any());

        // when & then
        assertThatThrownBy(() -> postService.deletePostById(111L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }
}