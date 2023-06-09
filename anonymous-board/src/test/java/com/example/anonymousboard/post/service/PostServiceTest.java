package com.example.anonymousboard.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.auth.exception.AuthorizationException;
import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.comment.dto.CommentResponse;
import com.example.anonymousboard.comment.exception.CommentLimitException;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.domain.Password;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.dto.PagePostsDetailResponse;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostDetailResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.InvalidPostKeywordException;
import com.example.anonymousboard.post.exception.PostLimitException;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.util.ServiceTest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

class PostServiceTest extends ServiceTest {

    Comment comment;

    Post post1;

    Post post2;

    Post post3;

    Post post4;

    Post keywordPost1;

    Post keywordPost2;

    Page<Post> pagePosts;

    Member member;

    AuthInfo authInfo;

    @BeforeEach
    void setUp() {
        member = new Member(1L, "valid@mail.com", Password.of(encryptor, "!qwer123"));
        post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .member(member)
                .build();
        post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .member(member)
                .build();
        post3 = Post.builder()
                .title("제목3")
                .content("내용3")
                .member(member)
                .build();
        post4 = Post.builder()
                .title("제목4")
                .content("내용4")
                .member(member)
                .build();
        keywordPost1 = Post.builder()
                .title("비슷한 제목")
                .content("내용4")
                .member(member)
                .build();
        keywordPost2 = Post.builder()
                .title("비슷한2 제목")
                .content("내용4")
                .member(member)
                .build();
        comment = Comment.builder()
                .content("댓글")
                .member(member)
                .post(post1)
                .build();
        authInfo = new AuthInfo(1L);

        pagePosts = new PageImpl<>(List.of(post4, post3, post2, post1));
    }

    @DisplayName("게시글을 저장한다.")
    @Test
    void createPost_success() {
        // given
        given(postRepository.save(any(Post.class))).willReturn(post1);
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(member));
        PostSaveRequest saveRequest = PostSaveRequest.builder()
                .title("제목1")
                .content("내용1")
                .build();

        // when
        PostSaveResponse saveResponse = postService.createPost(authInfo, saveRequest);

        // then
        assertAll(
                () -> assertThat(saveResponse.getMessage()).isEqualTo("게시글 작성을 완료했습니다.")
        );

    }

    @DisplayName("존재하지 않는 사용자라면 게시글을 저장할 수 없다.")
    @Test
    void createPost_exception_notFoundMember() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());
        PostSaveRequest saveRequest = PostSaveRequest.builder()
                .title("제목1")
                .content("내용1")
                .build();

        // when & then
        assertThatThrownBy(() -> postService.createPost(authInfo, saveRequest))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessageContaining("회원을 찾을 수 없습니다.");
    }

    @DisplayName("전체 게시글을 조회할 수 있다.")
    @Test
    void findPosts() {
        // given
        given(postRepository.findPostsByOrderByCreatedAtDesc(any())).willReturn(pagePosts);
        given(commentRepository.findCommentsPagesByPostIn(any())).willReturn(List.of(comment));
        Pageable pageable = PageRequest.of(0, 100, Direction.DESC, "createdAt");

        // when
        PagePostsDetailResponse posts = postService.findPosts(100, pageable);
        PostDetailResponse postDetailResponse = posts.getPosts().get(0);
        List<String> titles = posts.getPosts()
                .stream()
                .map(PostDetailResponse::getTitle)
                .collect(Collectors.toList());
        List<CommentResponse> comments = posts.getPosts()
                .get(3)
                .getComments();

        // then
        assertAll(
                () -> assertThat(posts.getTotalPostCount()).isEqualTo(4),
                () -> assertThat(titles).containsExactly("제목4", "제목3", "제목2", "제목1"),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo("제목4"),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo("내용4"),
                () -> assertThat(comments.size()).isEqualTo(1),
                () -> assertThat(comments.get(0).getContent()).isEqualTo("댓글")
        );
    }

    @DisplayName("전체 게시글의 개수를 100개 넘게 조회할 수 없다.")
    @Test
    void findPosts_exception_invalidPostSize() {
        // given
        Pageable pageable = PageRequest.of(0, 101, Direction.DESC, "createdAt");

        // when & then
        assertThatThrownBy(() -> postService.findPosts(100, pageable))
                .isInstanceOf(PostLimitException.class)
                .hasMessageContaining("게시글은 최대 100개까지 조회할 수 있습니다.");
    }

    @DisplayName("전체 게시글 조회시 게시글당 댓글 조회 개수를 100개 넘게 조회할 수 없다.")
    @Test
    void findPosts_exception_invalidCommentSize() {
        // given
        Pageable pageable = PageRequest.of(0, 100, Direction.DESC, "createdAt");

        // when & then
        assertThatThrownBy(() -> postService.findPosts(101, pageable))
                .isInstanceOf(CommentLimitException.class)
                .hasMessageContaining("댓글은 최대 100개까지 조회할 수 있습니다.");
    }

    @DisplayName("특정 id값의 게시글을 조회할 수 있다.")
    @Test
    void findPostById() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(post1));
        given(commentRepository.findCommentsByPost(post1)).willReturn(List.of(comment));

        // when
        PostDetailResponse findPost = postService.findPostDetailById(1L);

        // then
        assertAll(
                () -> assertThat(findPost.getTitle()).isEqualTo("제목1"),
                () -> assertThat(findPost.getContent()).isEqualTo("내용1"),
                () -> assertThat(findPost.getComments().size()).isEqualTo(1),
                () -> assertThat(findPost.getComments().get(0).getContent()).isEqualTo("댓글"),
                () -> assertThat(findPost.getComments().get(0).getEmail()).isEqualTo("valid@mail.com")
        );
    }

    @DisplayName("존재하지 않는 id의 게시글을 조회하면 예외가 발생한다.")
    @Test
    void findPostById_exception_notFoundPostId() {
        // when & then
        assertThatThrownBy(() -> postService.findPostDetailById(11111L))
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
        postService.updatePostById(authInfo, 1L, updateRequest);
        PostResponse updatedPost = postService.findPostById(1L);

        // then
        assertAll(
                () -> assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목"),
                () -> assertThat(updatedPost.getContent()).isEqualTo("수정된 내용"),
                () -> assertThat(updatedPost.getCreatedAt()).isEqualTo(post1.getCreatedAt())
        );
    }

    @DisplayName("특정 게시글의 내용을 공백으로 수정할 수 있다.")
    @Test
    void updatePost_with_blankContent() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post1));
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content(" ")
                .build();

        // when
        postService.updatePostById(authInfo, 1L, updateRequest);
        PostResponse updatedPost = postService.findPostById(1L);

        // then
        assertAll(
                () -> assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목"),
                () -> assertThat(updatedPost.getContent()).isEqualTo(" "),
                () -> assertThat(updatedPost.getCreatedAt()).isEqualTo(post1.getCreatedAt())
        );
    }

    @DisplayName("존재하지 않는 게시글을 수정할 수 없다.")
    @Test
    void updatePost_exception_notFoundPostId() {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when & then
        assertThatThrownBy(() -> postService.updatePostById(authInfo, 1111L, updateRequest))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @DisplayName("권한이 없는 게시글을 수정할 수 없다.")
    @Test
    void updatePost_exception_noAuthorization() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post1));
        AuthInfo otherAuth = new AuthInfo(2L);
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();

        // when & then
        assertThatThrownBy(() -> postService.updatePostById(otherAuth, 1L, updateRequest))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageContaining("권한이 없습니다.");
    }

    @DisplayName("특정 게시글을 삭제할 수 있다.")
    @Test
    void deletePost() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.of(post1));
        doNothing().when(postRepository)
                .delete(any());

        assertThatNoException().isThrownBy(() -> postService.deletePostById(authInfo, 1L));
    }

    @DisplayName("존재하지 않는 게시글은 삭제할 수 없다.")
    @Test
    void deletePost_exception_notFoundPostId() {
        // given
        given(postRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> postService.deletePostById(authInfo, 111L))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시글을 찾을 수 없습니다.");
    }

    @DisplayName("권한이 없는 게시글은 삭제할 수 없다.")
    @Test
    void deletePost_exception_noAuthorization() {
        // given
        AuthInfo otherAuth = new AuthInfo(2L);
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(post1));

        // when & then
        assertThatThrownBy(() -> postService.deletePostById(otherAuth, 111L))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageContaining("권한이 없습니다.");
    }

    @DisplayName("특정 키워드를 통해 게시글을 검색할 수 있다.")
    @Test
    void findPostsByKeyword_with_keyword() {
        // given
        given(postRepository.findPostsByTitleValueContaining(any(), any())).willReturn(
                List.of(keywordPost1, keywordPost2));

        // when
        PagePostsResponse pagePostsResponse = postService.findPostsByKeyword("비슷한",
                PageRequest.of(0, 100, Direction.DESC, "createdAt"));
        List<String> titles = pagePostsResponse.getPostResponses().stream()
                .map(PostResponse::getTitle)
                .collect(Collectors.toList());

        // then
        assertThat(titles).containsExactly("비슷한 제목", "비슷한2 제목");
    }

    @DisplayName("키워드가 조건을 충족하지 않는다면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "    ", "    "})
    void findPostsByKeyword_invalidKeyword(String invalidKeyword) {
        // when & then
        assertThatThrownBy(() -> postService.findPostsByKeyword(invalidKeyword,
                PageRequest.of(0, 100, Direction.DESC, "createdAt")))
                .isInstanceOf(InvalidPostKeywordException.class)
                .hasMessageContaining("검색 키워드는 공백을 입력할 수 없으며, 1글자 이상 입력해야 합니다.");
    }
}