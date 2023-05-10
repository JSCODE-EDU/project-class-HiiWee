package com.example.anonymousboard.post.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.post.domain.Content;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.domain.Title;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.exception.InvalidContentException;
import com.example.anonymousboard.post.exception.InvalidTitleException;
import com.example.anonymousboard.post.exception.PostErrorCode;
import com.example.anonymousboard.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PostService postService;


    PagePostsResponse pagePostsResponse;

    Post post1;

    Post post2;

    Post post3;

    @BeforeEach
    void setUp() {
        post1 = Post.builder()
                .id(1L)
                .title(Title.from("제목1"))
                .content(Content.from("내용1"))
                .build();
        post2 = Post.builder()
                .id(2L)
                .title(Title.from("제목2"))
                .content(Content.from("내용2"))
                .build();
        post3 = Post.builder()
                .id(3L)
                .title(Title.from("제목3"))
                .content(Content.from("내용3"))
                .build();
        pagePostsResponse = PagePostsResponse.of(List.of(post3, post2, post1));
    }

    @DisplayName("게시글 작성을 하면 201을 반환한다.")
    @Test
    void createPost() throws Exception {
        // given
        PostSaveRequest post = PostSaveRequest.builder()
                .title("게시글 제목 입니다.")
                .content("게시글 내용 입니다.")
                .build();
        doReturn(1L).when(postService)
                .createPost(any());

        // when & then
        mockMvc.perform(post("/posts")
                        .content(objectMapper.writeValueAsString(post))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.savedId").value(1L),
                        jsonPath("$.message").value("게시글 작성을 완료했습니다.")
                );
    }

    @DisplayName("게시글 제목이 200글자를 넘기면 400을 반환한다.")
    @Test
    void createPost_exception_invalidTitle() throws Exception {
        // given
        String title = "A".repeat(201);
        PostSaveRequest post = PostSaveRequest.builder()
                .title(title)
                .content("게시글 내용 입니다.")
                .build();
        doThrow(new InvalidTitleException()).when(postService)
                .createPost(any());

        // when & then
        mockMvc.perform(post("/posts")
                        .content(objectMapper.writeValueAsString(post))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.errorCode").value(PostErrorCode.INVALID_TITLE.value()),
                        jsonPath("$.message").value("게시글 제목은 200자 이하여야 합니다.")
                );
    }

    @DisplayName("게시글 본문이 5000글자를 넘기면 400을 반환한다.")
    @Test
    void createPost_exception_invalidContent() throws Exception {
        // given
        String content = "A".repeat(5001);
        PostSaveRequest post = PostSaveRequest.builder()
                .title("게시글 제목입니다.")
                .content(content)
                .build();
        doThrow(new InvalidContentException()).when(postService)
                .createPost(any());

        // when & then
        mockMvc.perform(post("/posts")
                        .content(objectMapper.writeValueAsString(post))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.errorCode").value(PostErrorCode.INVALID_CONTENT.value()),
                        jsonPath("$.message").value("게시글 본문은 5000자 이하여야 합니다.")
                );
    }

    @DisplayName("모든 게시글을 조회할 수 있으며 200을 반환한다.")
    @Test
    void findPosts() throws Exception {
        // given
        doReturn(pagePostsResponse).when(postService)
                .findPosts(any());

        // when & then
        mockMvc.perform(get("/posts"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.postResponses.size()").value(3),
                        jsonPath("$.postResponses[0].id").value(3L),
                        jsonPath("$.postResponses[1].id").value(2L),
                        jsonPath("$.postResponses[2].id").value(1L)
                );
    }
}