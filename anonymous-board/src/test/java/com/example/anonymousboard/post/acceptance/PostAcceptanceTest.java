package com.example.anonymousboard.post.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.anonymousboard.advice.CommonErrorCode;
import com.example.anonymousboard.advice.ErrorResponse;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PostAcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;

    PostSaveRequest postSaveRequest1;

    PostSaveRequest postSaveRequest2;

    PostSaveRequest postSaveRequest3;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        postSaveRequest1 = PostSaveRequest.builder().title("제목1").content("내용1").build();
        postSaveRequest2 = PostSaveRequest.builder().title("제목2").content("내용2").build();
        postSaveRequest3 = PostSaveRequest.builder().title("제목3").content("내용3").build();
    }

    @DisplayName("게시글 작성을 할 수 있다.")
    @Test
    void createPost() throws JsonProcessingException {
        // given
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("게시글 제목입니다.")
                .content("게시글 내용입니다.")
                .build();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .body(objectMapper.writeValueAsString(postSaveRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/posts")
                .then().log().all()
                .extract();
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
    void createPost_exception_emptyTitle() throws JsonProcessingException {
        // given
        PostSaveRequest postSaveRequest = PostSaveRequest.builder()
                .title("")
                .content("게시글 내용입니다.")
                .build();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .body(objectMapper.writeValueAsString(postSaveRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/posts")
                .then().log().all()
                .extract();
        ErrorResponse errorResponse = response.jsonPath().getObject(".", ErrorResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(errorResponse.getMessage()).isEqualTo("제목을 반드시 입력해야 합니다."),
                () -> assertThat(errorResponse.getErrorCode()).isEqualTo(
                        CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value())
        );
    }

    @DisplayName("게시글을 작성하고 작성한 모든 게시글을 조회할 수 있다.")
    @Test
    void findPosts() throws JsonProcessingException {
        // given
        given().log().all()
                .body(objectMapper.writeValueAsString(postSaveRequest1))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/posts")
                .then().log().all();
        given().log().all()
                .body(objectMapper.writeValueAsString(postSaveRequest2))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/posts")
                .then().log().all();
        given().log().all()
                .body(objectMapper.writeValueAsString(postSaveRequest3))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/posts")
                .then().log().all();

        // when
        ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/posts")
                .then().log().all()
                .extract();
        PagePostsResponse postsResponse = response.jsonPath().getObject(".", PagePostsResponse.class);
        List<String> titles = postsResponse.getPostResponses()
                .stream()
                .map(PostResponse::getTitle)
                .collect(Collectors.toList());

        // then
        assertAll(
                () -> assertThat(postsResponse.getTotalPostCount()).isEqualTo(3),
                () -> assertThat(titles).containsExactly("제목3", "제목2", "제목1")
        );
    }

    @DisplayName("게시글은 최대 100개까지 조회할 수 있다.")
    @Test
    void findPosts_withLimit() throws JsonProcessingException {
        // given
        for (int sequence = 1; sequence <= 200; sequence++) {
            given().body(objectMapper.writeValueAsString(postSaveRequest1))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .post("/posts");
        }

        // when
        ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/posts")
                .then().log().all()
                .extract();
        PagePostsResponse postsResponse = response.jsonPath().getObject(".", PagePostsResponse.class);

        // then
        assertThat(postsResponse.getTotalPostCount()).isEqualTo(100);
    }
}
