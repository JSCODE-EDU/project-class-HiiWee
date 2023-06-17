package com.example.anonymousboard.post.acceptance;

import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpDeleteOne;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpGet;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpGetFindAllWithParameter;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPost;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPostAllWithAuthorization;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPostWithAuthorization;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPutUpdateOne;
import static com.example.anonymousboard.util.fixture.TokenFixture.getMemberToken;
import static com.example.anonymousboard.util.fixture.TokenFixture.getOtherMemberToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.advice.CommonErrorCode;
import com.example.anonymousboard.advice.ErrorResponse;
import com.example.anonymousboard.auth.exception.AuthErrorCode;
import com.example.anonymousboard.post.dto.PagePostsDetailResponse;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostDetailResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.PostErrorCode;
import com.example.anonymousboard.util.AcceptanceTest;
import com.example.anonymousboard.util.fixture.TokenFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class PostAcceptanceTest extends AcceptanceTest {

    PostSaveRequest postSaveRequest1;

    PostSaveRequest postSaveRequest2;

    PostSaveRequest postSaveRequest3;

    PostUpdateRequest postUpdateRequest;

    @BeforeEach
    void setUp() {
        System.out.println("passsssss");
        postSaveRequest1 = PostSaveRequest.builder().title("제목1").content("내용1").build();
        postSaveRequest2 = PostSaveRequest.builder().title("제목2").content("내용2").build();
        postSaveRequest3 = PostSaveRequest.builder().title("제목3").content("내용3").build();
        postUpdateRequest = PostUpdateRequest.builder().title("수정된 제목").content("수정된 내용").build();
    }

    @DisplayName("게시글 작성을 할 수 있다.")
    @Test
    void createPost() {
        // given
        String token = getMemberToken();

        // when
        ExtractableResponse<Response> response = httpPostWithAuthorization(postSaveRequest1, "/posts", token);
        PostSaveResponse postSaveResponse = response.jsonPath().getObject(".", PostSaveResponse.class);

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(postSaveResponse.getSavedId()).isEqualTo(1L),
                () -> assertThat(postSaveResponse.getMessage()).isEqualTo("게시글 작성을 완료했습니다.")
        );
    }


    @DisplayName("공백 내용으로 게시글 작성을 할 수 있다.")
    @Test
    void createPost_with_blankContent() {
        // given
        String token = getMemberToken();
        PostSaveRequest postSaveRequest = PostSaveRequest.builder().title("제목").content("  ").build();

        // when
        ExtractableResponse<Response> response = httpPostWithAuthorization(postSaveRequest, "/posts", token);
        PostSaveResponse postSaveResponse = response.jsonPath().getObject(".", PostSaveResponse.class);

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(postSaveResponse.getSavedId()).isEqualTo(1L),
                () -> assertThat(postSaveResponse.getMessage()).isEqualTo("게시글 작성을 완료했습니다.")
        );
    }

    @DisplayName("빈 제목으로 게시글을 작성할 수 없다.")
    @Test
    void createPost_exception_emptyTitle() {
        // given
        String token = getMemberToken();
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("")
                .content("게시글 내용입니다.")
                .build();

        // when
        ExtractableResponse<Response> response = httpPostWithAuthorization(postSaveRequest, "/posts", token);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("제목을 반드시 입력해야 합니다."),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(
                        CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value())
        );
    }

    @DisplayName("빈 내용으로 게시글을 작성할 수 없다.")
    @Test
    void createPost_exception_emptyContent() {
        // given
        String token = getMemberToken();
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("게시글 제목입니다.")
                .content("")
                .build();

        // when
        ExtractableResponse<Response> response = httpPostWithAuthorization(postSaveRequest, "/posts", token);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("내용을 반드시 입력해야 합니다."),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(
                        CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value())
        );
    }

    @DisplayName("로그인을 하지 않았다면 게시글을 작성할 수 없다.")
    @Test
    void createPost_exception_notLogin() {
        // given
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        ExtractableResponse<Response> response = httpPost(postSaveRequest, "/posts");
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        assertAll(
                () -> assertThat(errorResponse.getMessage()).isEqualTo("인증을 위한 헤더를 찾을 수 없습니다."),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(AuthErrorCode.NO_AUTHORIZATION_HEADER.value())
        );
    }

    @DisplayName("게시글을 작성하고 작성한 모든 게시글을 조회할 수 있다.")
    @Test
    void findPosts() {
        // given
        String token = getMemberToken();
        httpPostAllWithAuthorization(
                "/posts",
                token,
                postSaveRequest1,
                postSaveRequest2,
                postSaveRequest3
        );

        // when
        ExtractableResponse<Response> response = httpGet("/posts");
        PagePostsDetailResponse postsResponse = response.jsonPath().getObject(".", PagePostsDetailResponse.class);
        List<String> titles = postsResponse.getPosts()
                .stream()
                .map(PostDetailResponse::getTitle)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(postsResponse.getTotalPostCount()).isEqualTo(3),
                () -> assertThat(titles).containsExactly("제목3", "제목2", "제목1")
        );
    }

    @DisplayName("게시글은 최대 100개까지 조회할 수 있다.")
    @Test
    void findPosts_with_limit100() {
        // given
        String token = getMemberToken();
        for (int sequence = 1; sequence <= 101; sequence++) {
            httpPostWithAuthorization(postSaveRequest1, "/posts", token);
        }

        // when
        ExtractableResponse<Response> response = httpGet("/posts");
        PagePostsDetailResponse pagePostsDetailResponse = response.jsonPath().getObject(".", PagePostsDetailResponse.class);

        // then
        assertThat(pagePostsDetailResponse.getTotalPostCount()).isEqualTo(100);
    }

    @DisplayName("특정 게시글 1개를 저장하고 조회할 수 있다.")
    @Test
    void findPost_with_createdPost() {
        // given
        String token = getMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpGet("/posts/1");
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        // then
        assertAll(
                () -> assertThat(postDetailResponse.getId()).isEqualTo(1L),
                () -> assertThat(postDetailResponse.getTitle()).isEqualTo("제목1"),
                () -> assertThat(postDetailResponse.getContent()).isEqualTo("내용1"),
                () -> assertThat(postDetailResponse.getCreatedAt()).isNotNull(),
                () -> assertThat(postDetailResponse.getComments().size()).isEqualTo(0)
        );
    }

    @DisplayName("없는 게시글은 조회할 수 없다.")
    @Test
    void findPost_exception_noPost() {
        // when
        ExtractableResponse<Response> response = httpGet("/posts/1");
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("게시글을 찾을 수 없습니다.")
        );
    }

    @DisplayName("작성한 게시글을 수정할 수 있다.")
    @Test
    void updatePost_with_createdPost() {
        // given
        String token = getMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpPutUpdateOne(postUpdateRequest, "/posts/1", token);
        PostResponse postResponse = response.jsonPath().getObject(".", PostResponse.class);

        // then
        assertAll(
                () -> assertThat(postResponse.getId()).isEqualTo(1L),
                () -> assertThat(postResponse.getTitle()).isEqualTo("수정된 제목"),
                () -> assertThat(postResponse.getContent()).isEqualTo("수정된 내용"),
                () -> assertThat(postResponse.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("작성한 게시글의 내용을 공백으로 수정할 수 있다.")
    @Test
    void updatePost_with_blankContent() {
        // given
        String token = getMemberToken();
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder().title("수정할 제목").content("    ").build();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpPutUpdateOne(postUpdateRequest, "/posts/1", token);
        PostResponse postResponse = response.jsonPath().getObject(".", PostResponse.class);

        // then
        assertAll(
                () -> assertThat(postResponse.getId()).isEqualTo(1L),
                () -> assertThat(postResponse.getTitle()).isEqualTo("수정할 제목"),
                () -> assertThat(postResponse.getContent()).isEqualTo("    "),
                () -> assertThat(postResponse.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("제목이 비어있다면 수정할 수 없다.")
    @Test
    void updatePost_exception_invalidTitle() {
        // given
        String token = getMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder().title("").content("내용만 존재").build();

        // when
        ExtractableResponse<Response> response = httpPutUpdateOne(postUpdateRequest, "/posts/1", token);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(
                        CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("제목을 반드시 입력해야 합니다.")
        );
    }

    @DisplayName("내용이 비어있다면 수정할 수 없다.")
    @Test
    void updatePost_exception_invalidContent() {
        // given
        String token = getMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);
        PostUpdateRequest postUpdateRequest = PostUpdateRequest.builder().title("제목만 존재").content("").build();

        // when
        ExtractableResponse<Response> response = httpPutUpdateOne(postUpdateRequest, "/posts/1", token);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(
                        CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("내용을 반드시 입력해야 합니다.")
        );
    }

    @DisplayName("게시글을 작성한 사용자가 아니라면 게시글을 수정할 수 없다.")
    @Test
    void updatePost_exception_noAuthorization() {
        // given
        String token = getMemberToken();
        String otherToken = TokenFixture.getOtherMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpPutUpdateOne(postUpdateRequest, "/posts/1", otherToken);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getMessage()).isEqualTo("권한이 없습니다."),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(AuthErrorCode.AUTHORIZATION.value())
        );
    }

    @DisplayName("특정 게시글을 작성하고 삭제할 수 있다.")
    @Test
    void deletePost_with_createdPost() {
        // given
        String token = getMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpDeleteOne("/posts/1", token);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("없는 게시글은 삭제할 수 없다.")
    @Test
    void deletePost_exception_notFoundPostId() {
        // given
        String token = getMemberToken();

        // when
        ExtractableResponse<Response> response = httpDeleteOne("/posts/1", token);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("게시글을 찾을 수 없습니다.")
        );
    }

    @DisplayName("게시글을 작성한 사용자가 아니면 게시글을 삭제할 수 없다.")
    @Test
    void deletePost_exception_noAuthorization() {
        // given
        String token = getMemberToken();
        String otherToken = getOtherMemberToken();
        httpPostWithAuthorization(postSaveRequest1, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpDeleteOne("/posts/1", otherToken);
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getMessage()).isEqualTo("권한이 없습니다."),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(AuthErrorCode.AUTHORIZATION.value())
        );
    }

    @DisplayName("게시글을 저장하고 키워드를 가지고 특정 게시글만 조회할 수 있다.")
    @Test
    void findPostsByKeyword_with_keyword() {
        // given
        String token = getMemberToken();
        PostSaveRequest customRequest = PostSaveRequest.builder()
                .title("특정 키워드")
                .content("내용")
                .build();
        httpPostAllWithAuthorization(
                "/posts",
                token,
                customRequest,
                postSaveRequest1,
                postSaveRequest2,
                postSaveRequest3
        );

        // when
        ExtractableResponse<Response> response = httpGetFindAllWithParameter("/posts/search", "keyword", "특정");
        PagePostsResponse pagePostsResponse = response.jsonPath().getObject(".", PagePostsResponse.class);
        PostResponse postResponse = pagePostsResponse.getPostResponses().get(0);

        // then
        assertAll(
                () -> assertThat(postResponse.getId()).isEqualTo(1L),
                () -> assertThat(postResponse.getTitle()).isEqualTo("특정 키워드"),
                () -> assertThat(postResponse.getContent()).isEqualTo("내용"),
                () -> assertThat(postResponse.getCreatedAt()).isNotNull()
        );
    }

    @DisplayName("게시글을 저장후 잘못된 검색 키워드로 조회하면 실패한다.")
    @Test
    void findPostsByKeyword_exception_invalidKeyword() {
        // given
        String token = getMemberToken();
        PostSaveRequest customRequest = PostSaveRequest.builder()
                .title("특정 키워드")
                .content("내용")
                .build();
        httpPostWithAuthorization(customRequest, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpGetFindAllWithParameter("/posts/search", "keyword", " ");
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(PostErrorCode.INVALID_POST_KEYWORD.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("검색 키워드는 공백을 입력할 수 없으며, 1글자 이상 입력해야 합니다.")
        );
    }

    @DisplayName("특정 키워드를 통한 게시글 검색은 최대 100개까지 조회할 수 있다.")
    @Test
    void findPostsByKeyword_with_limit100() {
        // given
        String token = getMemberToken();
        for (int sequence = 1; sequence <= 101; sequence++) {
            httpPostWithAuthorization(postSaveRequest1, "/posts", token);
        }

        // when
        ExtractableResponse<Response> response = httpGetFindAllWithParameter("/posts/search", "keyword", "제목");
        PagePostsResponse postsResponse = response.jsonPath().getObject(".", PagePostsResponse.class);

        // then
        assertThat(postsResponse.getTotalPostCount()).isEqualTo(100);
    }
}
