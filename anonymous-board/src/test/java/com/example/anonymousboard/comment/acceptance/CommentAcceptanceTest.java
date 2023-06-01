package com.example.anonymousboard.comment.acceptance;

import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpGet;
import static com.example.anonymousboard.util.fixture.ApiRequestFixture.httpPostWithAuthorization;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.comment.dto.CommentSaveRequest;
import com.example.anonymousboard.comment.dto.CommentSaveResponse;
import com.example.anonymousboard.post.dto.PostDetailResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.util.AcceptanceTest;
import com.example.anonymousboard.util.fixture.TokenFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CommentAcceptanceTest extends AcceptanceTest {

    @DisplayName("특정 게시글에 댓글을 작성할 수 있다.")
    @Test
    void addComment() {
        // given
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("댓글");
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        String token = TokenFixture.getMemberToken();
        String otherToken = TokenFixture.getOtherMemberToken();
        httpPostWithAuthorization(postSaveRequest, "/posts", token);

        // when
        ExtractableResponse<Response> response = httpPostWithAuthorization(commentSaveRequest, "/posts/1/comments",
                otherToken);
        CommentSaveResponse commentSaveResponse = response.jsonPath().getObject(".", CommentSaveResponse.class);

        // then
        assertThat(commentSaveResponse.getSavedId()).isEqualTo(1L);
    }

    @DisplayName("게시글에 댓글을 작성후 게시글 조회시 모든 댓글을 조회할 수 있다.")
    @Test
    void findPostDetail_with_comments() {
        // given
        CommentSaveRequest commentSaveRequest = new CommentSaveRequest("댓글");
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        String token = TokenFixture.getMemberToken();
        String otherToken = TokenFixture.getOtherMemberToken();
        httpPostWithAuthorization(postSaveRequest, "/posts", token);
        httpPostWithAuthorization(commentSaveRequest, "/posts/1/comments", otherToken);

        // when
        ExtractableResponse<Response> response = httpGet("/posts/1");
        PostDetailResponse postDetailResponse = response.jsonPath().getObject(".", PostDetailResponse.class);

        assertAll(
                () -> assertThat(postDetailResponse.getComments().size()).isEqualTo(1),
                () -> assertThat(postDetailResponse.getComments().get(0).getContent()).isEqualTo("댓글"),
                () -> assertThat(postDetailResponse.getComments().get(0).getEmail()).isEqualTo("valid02@mail.com")
        );
    }
}
