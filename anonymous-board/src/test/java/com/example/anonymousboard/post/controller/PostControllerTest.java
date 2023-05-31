package com.example.anonymousboard.post.controller;

import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.anonymousboard.util.apidocs.DocumentFormatGenerator.getConstraints;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.advice.CommonErrorCode;
import com.example.anonymousboard.auth.exception.AuthErrorCode;
import com.example.anonymousboard.auth.exception.AuthorizationException;
import com.example.anonymousboard.comment.dto.CommentResponse;
import com.example.anonymousboard.member.exception.MemberErrorCode;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostDetailResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.InvalidContentException;
import com.example.anonymousboard.post.exception.InvalidPostKeywordException;
import com.example.anonymousboard.post.exception.InvalidTitleException;
import com.example.anonymousboard.post.exception.PostErrorCode;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.util.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.http.auth.AUTH;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class PostControllerTest extends ControllerTest {

    PagePostsResponse pagePostsResponse;

    PagePostsResponse keywordPosts;

    PostDetailResponse postDetailResponse;

    CommentResponse commentResponse1;

    CommentResponse commentResponse2;

    CommentResponse commentResponse3;

    PostResponse postResponse1;

    PostResponse postResponse2;

    PostResponse postResponse3;

    PostResponse keywordPostResponse1;

    PostResponse keywordPostResponse2;

    @BeforeEach
    void setUp() {
        commentResponse1 = CommentResponse.builder()
                .content("댓글1")
                .email("valid01@mail.com")
                .createdAt(LocalDateTime.now())
                .build();
        commentResponse2 = CommentResponse.builder()
                .content("댓글2")
                .email("valid02@mail.com")
                .createdAt(LocalDateTime.now())
                .build();
        commentResponse3 = CommentResponse.builder()
                .content("댓글3")
                .email("valid03@mail.com")
                .createdAt(LocalDateTime.now())
                .build();
        postDetailResponse = PostDetailResponse.builder()
                .id(1L)
                .title("제목1")
                .content("내용1")
                .createdAt(LocalDateTime.now())
                .comments(List.of(commentResponse1, commentResponse2, commentResponse3))
                .build();

        postResponse1 = PostResponse.builder()
                .id(1L)
                .title("제목1")
                .content("내용1")
                .createdAt(LocalDateTime.now())
                .build();
        postResponse2 = PostResponse.builder()
                .id(2L)
                .title("제목2")
                .content("내용2")
                .createdAt(LocalDateTime.now())
                .build();
        postResponse3 = PostResponse.builder()
                .id(3L)
                .title("제목3")
                .content("내용3")
                .createdAt(LocalDateTime.now())
                .build();
        keywordPostResponse1 = PostResponse.builder()
                .id(4L)
                .title("비슷한 제목")
                .content("내용")
                .createdAt(LocalDateTime.now())
                .build();
        keywordPostResponse2 = PostResponse.builder()
                .id(5L)
                .title("비슷한2 제목")
                .content("내용")
                .createdAt(LocalDateTime.now())
                .build();

        pagePostsResponse = PagePostsResponse.builder()
                .postResponses(List.of(postResponse3, postResponse2, postResponse1))
                .totalPostCount(3)
                .build();
        keywordPosts = PagePostsResponse.builder()
                .postResponses(List.of(keywordPostResponse2, keywordPostResponse1))
                .totalPostCount(3)
                .build();
    }

    @DisplayName("게시글 작성을 하면 201을 반환한다.")
    @Test
    void createPost() throws Exception {
        // given
        PostSaveResponse saveResponse = PostSaveResponse.createPostSuccess(1L);
        PostSaveRequest post = PostSaveRequest.builder()
                .title("게시글 제목 입니다.")
                .content("게시글 내용 입니다.")
                .build();
        given(postService.createPost(any(), any())).willReturn(saveResponse);

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isCreated(),
                jsonPath("$.savedId").value(1L),
                jsonPath("$.message").value("게시글 작성을 완료했습니다.")
        ).andDo(
                document("post/create/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("savedId").type(JsonFieldType.NUMBER).description("저장된 게시글 id"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("게시글 저장 성공 메시지")
                        )
                )
        );
    }

    @DisplayName("게시글 제목이 비어있는 경우 400을 반환한다.")
    @Test
    void createPost_exception_emptyTitle() throws Exception {
        // given
        String title = "";
        PostSaveRequest post = PostSaveRequest.builder()
                .title(title)
                .content("게시글 내용 입니다.")
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value()),
                jsonPath("$.message").value("제목을 반드시 입력해야 합니다.")
        ).andDo(
                document("post/create/fail/emptyTitle",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("게시글 제목이 15글자를 넘기면 400을 반환한다.")
    @Test
    void createPost_exception_invalidTitle() throws Exception {
        // given
        String title = "15글자가 넘어가는 게시글의 제목입니다.";
        PostSaveRequest post = PostSaveRequest.builder()
                .title(title)
                .content("게시글 내용 입니다.")
                .build();
        given(postService.createPost(any(), any())).willThrow(new InvalidTitleException());

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(PostErrorCode.INVALID_TITLE.value()),
                jsonPath("$.message").value("게시글 제목은 1자 이상 15자 이하까지 입력할 수 있습니다.")
        ).andDo(
                document("post/create/fail/tooLongTitle",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("게시글 본문이 비어있는 경우 400을 반환한다.")
    @Test
    void createPost_exception_emptyContent() throws Exception {
        // given
        String content = "";
        PostSaveRequest post = PostSaveRequest.builder()
                .title("게시글 제목입니다.")
                .content(content)
                .build();

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value()),
                jsonPath("$.message").value("내용을 반드시 입력해야 합니다.")
        ).andDo(
                document("post/create/fail/emptyContent",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("게시글 본문이 1000글자를 넘기면 400을 반환한다.")
    @Test
    void createPost_exception_invalidContent() throws Exception {
        // given
        String content = "A".repeat(1001);
        PostSaveRequest post = PostSaveRequest.builder()
                .title("게시글 제목입니다.")
                .content(content)
                .build();
        given(postService.createPost(any(), any())).willThrow(new InvalidContentException());

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(PostErrorCode.INVALID_CONTENT.value()),
                jsonPath("$.message").value("게시글 내용은 1자 이상 1000자 이하까지 입력할 수 있습니다.")
        ).andDo(
                document("post/create/fail/tooLongContent",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("존재하지 않는 사용자가 게시글을 작성하면 404를 반환한다.")
    @Test
    void createPost_exception_notFoundMember() throws Exception {
        // given
        PostSaveRequest post = PostSaveRequest.builder()
                .title("게시글 제목 입니다.")
                .content("게시글 내용 입니다.")
                .build();
        given(postService.createPost(any(), any())).willThrow(new MemberNotFoundException());

        // when
        ResultActions result = mockMvc.perform(post("/posts")
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(post))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(status().isNotFound(),
                jsonPath("$.message").value("회원을 찾을 수 없습니다."),
                jsonPath("$.errorCode").value(MemberErrorCode.MEMBER_NOT_FOUND.value())
        ).andDo(
                document("post/create/fail/notFoundMember",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );

    }

    @DisplayName("모든 게시글을 조회할 수 있으며 200을 반환한다.")
    @Test
    void findPosts() throws Exception {
        // given
        given(postService.findPosts(any())).willReturn(pagePostsResponse);

        // when
        ResultActions result = mockMvc.perform(get("/posts"));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.postResponses.size()").value(3),
                jsonPath("$.postResponses[0].id").value(3L),
                jsonPath("$.postResponses[1].id").value(2L),
                jsonPath("$.postResponses[2].id").value(1L),
                jsonPath("$.totalPostCount").value(3)
        ).andDo(
                document("post/findAll/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("postResponses").type(JsonFieldType.ARRAY).description("게시글 조회 배열"),
                                fieldWithPath("postResponses[].id").type(JsonFieldType.NUMBER).description("게시글 ID")
                                        .optional(),
                                fieldWithPath("postResponses[].title").type(JsonFieldType.STRING).description("게시글 제목")
                                        .optional(),
                                fieldWithPath("postResponses[].content").type(JsonFieldType.STRING)
                                        .description("게시글 내용").optional(),
                                fieldWithPath("postResponses[].createdAt").type(JsonFieldType.STRING)
                                        .description("게시글 작성일자").optional(),
                                fieldWithPath("totalPostCount").type(JsonFieldType.NUMBER).description("조회한 게시글 개수")
                        )
                )
        );
    }

    @DisplayName("특정 게시글을 조회할 수 있으면 200을 반환한다.")
    @Test
    void findPost() throws Exception {
        // given
        given(postService.findPostDetailById(any())).willReturn(postDetailResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(1L),
                jsonPath("$.title").value("제목1"),
                jsonPath("$.content").value("내용1"),
                jsonPath("$.createdAt").isNotEmpty(),
                jsonPath("$.comments[0].email").value("valid01@mail.com"),
                jsonPath("$.comments[1].email").value("valid02@mail.com"),
                jsonPath("$.comments[2].email").value("valid03@mail.com")
        ).andDo(
                document("post/findById/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글 작성일자"),
                                fieldWithPath("comments[].content").type(JsonFieldType.STRING).description("해당 게시글 댓글 내용"),
                                fieldWithPath("comments[].email").type(JsonFieldType.STRING).description("해당 게시글 댓글 작성자 이메일"),
                                fieldWithPath("comments[].createdAt").type(JsonFieldType.STRING).description("해당 게시글 댓글 작성일자")
                        )
                )
        );
    }

    @DisplayName("특정 게시글을 조회할 수 없다면 400을 반환한다.")
    @Test
    void findPost_exception_notFoundPostId() throws Exception {
        // given
        given(postService.findPostDetailById(any())).willThrow(new PostNotFoundException());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L));

        // then
        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.errorCode").value(PostErrorCode.POST_NOT_FOUND.value()),
                jsonPath("$.message").value("게시글을 찾을 수 없습니다.")
        ).andDo(
                document("post/findById/fail/notFoundPost",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("특정 게시글을 수정할 수 있다면 200을 반환한다.")
    @Test
    void updatePost() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();
        PostResponse updatedPostResponse = PostResponse.builder()
                .id(1L)
                .title("수정된 제목")
                .content("수정된 내용")
                .createdAt(LocalDateTime.now())
                .build();
        given(postService.findPostById(any())).willReturn(updatedPostResponse);
        doNothing().when(postService)
                .updatePostById(any(), any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(1L),
                jsonPath("$.title").value("수정된 제목"),
                jsonPath("$.content").value("수정된 내용"),
                jsonPath("$.createdAt").isNotEmpty()
        ).andDo(
                document("post/update/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정될 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정될 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용"),
                                fieldWithPath("createdAt").type(JsonFieldType.STRING).description("게시글 작성일자")
                        )
                )
        );
    }

    @DisplayName("특정 게시글을 수정할 수 없다면 400을 반환한다.")
    @Test
    void updatePost_exception_notFoundPostId() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("제목1")
                .content("내용1")
                .build();
        doThrow(new PostNotFoundException()).when(postService)
                .updatePostById(any(), any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.errorCode").value(PostErrorCode.POST_NOT_FOUND.value()),
                jsonPath("$.message").value("게시글을 찾을 수 없습니다.")
        ).andDo(
                document("post/update/fail/notFoundPost",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정될 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정될 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("수정할 게시글 요청에서 제목이 비어있다면 400을 반환한다.")
    @Test
    void updatePost_exception_emptyTitle() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("")
                .content("내용1")
                .build();

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value()),
                jsonPath("$.message").value("제목을 반드시 입력해야 합니다.")
        ).andDo(
                document("post/update/fail/emptyTitle",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정될 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정될 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("수정할 게시글 요청에서 제목이 15글자를 넘긴다면 400을 반환한다.")
    @Test
    void updatePost_exception_tooLongTitle() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("15글자가 넘는 게시글 제목입니다.")
                .content("내용1")
                .build();
        doThrow(new InvalidTitleException()).when(postService)
                .updatePostById(any(), any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(PostErrorCode.INVALID_TITLE.value()),
                jsonPath("$.message").value("게시글 제목은 1자 이상 15자 이하까지 입력할 수 있습니다.")
        ).andDo(
                document("post/update/fail/tooLongTitle",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정될 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정될 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("수정할 게시글 요청에서 내용이 비어있다면 400을 반환한다.")
    @Test
    void updatePost_exception_emptyContent() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("제목")
                .content("")
                .build();

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value()),
                jsonPath("$.message").value("내용을 반드시 입력해야 합니다.")
        ).andDo(
                document("post/update/fail/emptyContent",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정될 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정될 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("수정할 게시글 요청에서 내용이 1000글자를 넘긴다면 400을 반환한다.")
    @Test
    void updatePost_exception_tooLongContent() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("제목")
                .content("A".repeat(1001))
                .build();
        doThrow(new InvalidContentException()).when(postService)
                .updatePostById(any(), any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(PostErrorCode.INVALID_CONTENT.value()),
                jsonPath("$.message").value("게시글 내용은 1자 이상 1000자 이하까지 입력할 수 있습니다.")
        ).andDo(
                document("post/update/fail/tooLongContent",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("수정될 제목")
                                        .attributes(getConstraints("constraints", "제목은 앞뒤 공백 제외 1 ~ 15자 사이여야 합니다.")),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("수정될 내용")
                                        .attributes(getConstraints("constraints", "내용은 공백 포함 1 ~ 1000자 사이여야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("게시글을 수정할 권한이 없다면 403을 반환한다.")
    @Test
    void updatePost_exception_noAuthorization() throws Exception {
        // given
        PostUpdateRequest updateRequest = PostUpdateRequest.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .build();
        doThrow(new AuthorizationException()).when(postService)
                .updatePostById(any(), any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.put("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any")
                .content(objectMapper.writeValueAsString(updateRequest))
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        // then
        result.andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").value("권한이 없습니다."),
                jsonPath("$.errorCode").value(AuthErrorCode.AUTHORIZATION.value())
        ).andDo(
                document("post/update/fail/noAuthorization",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        )
                )
        );
    }

    @DisplayName("특정 게시글을 삭제하면 204를 반환한다.")
    @Test
    void deletePost() throws Exception {
        // given
        doNothing().when(postService).deletePostById(any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any"));

        // then
        result.andExpectAll(status().isNoContent())
                .andDo(
                        document("post/delete/success",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                pathParameters(
                                        parameterWithName("postId").description("게시글 아이디")
                                )
                        )
                );
    }

    @DisplayName("특정 게시글을 삭제할 수 없다면 404를 반환한다.")
    @Test
    void deletePost_exception_notFountPostId() throws Exception {
        // given
        doThrow(new PostNotFoundException()).when(postService)
                .deletePostById(any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any"));

        // then
        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.errorCode").value(PostErrorCode.POST_NOT_FOUND.value()),
                jsonPath("$.message").value("게시글을 찾을 수 없습니다.")
        ).andDo(
                document("post/delete/fail/notFoundPost",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("게시글을 삭제할 권한이 없다면 403을 반환한다.")
    @Test
    void deletePost_exception_noAuthorization() throws Exception {
        // given
        doThrow(new AuthorizationException()).when(postService)
                .deletePostById(any(), any());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.delete("/posts/{postId}", 1L)
                .header(AUTHORIZATION, "any"));

        result.andExpectAll(
                status().isForbidden(),
                jsonPath("$.message").value("권한이 없습니다."),
                jsonPath("$.errorCode").value(AuthErrorCode.AUTHORIZATION.value())
        ).andDo(
                document("post/delete/fail/noAuthorization",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("특정 키워드를 기준으로 게시글을 조회하면 200을 반환한다.")
    @Test
    void findPostsByKeyword_with_keyword() throws Exception {
        // given
        given(postService.findPostsByKeyword(any(), any())).willReturn(keywordPosts);

        // when
        ResultActions result = mockMvc.perform(get("/posts").param("keyword", "비슷한"));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.postResponses.size()").value(2),
                jsonPath("$.postResponses[0].title").value("비슷한2 제목"),
                jsonPath("$.postResponses[1].title").value("비슷한 제목")
        ).andDo(
                document("post/findAllWithKeyword/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("keyword").description("검색할 제목 키워드").optional()
                                        .attributes(getConstraints("constraints", "검색 키워드는 공백 제외 1글자 이상이어야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("postResponses").type(JsonFieldType.ARRAY).description("게시글 조회 배열"),
                                fieldWithPath("postResponses[].id").type(JsonFieldType.NUMBER).description("게시글 ID")
                                        .optional(),
                                fieldWithPath("postResponses[].title").type(JsonFieldType.STRING).description("게시글 제목")
                                        .optional(),
                                fieldWithPath("postResponses[].content").type(JsonFieldType.STRING)
                                        .description("게시글 내용").optional(),
                                fieldWithPath("postResponses[].createdAt").type(JsonFieldType.STRING)
                                        .description("게시글 작성일자").optional(),
                                fieldWithPath("totalPostCount").type(JsonFieldType.NUMBER).description("조회한 게시글 개수")
                        )
                )
        );
    }

    @DisplayName("잘못된 키워드를 기준으로 게시글을 조회하면 400을 반환한다.")
    @Test
    void findPostsByKeyword_exception_InvalidKeyword() throws Exception {
        // given
        given(postService.findPostsByKeyword(any(), any())).willThrow(new InvalidPostKeywordException());

        // when
        ResultActions result = mockMvc.perform(get("/posts").param("keyword", ""));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.errorCode").value(PostErrorCode.INVALID_POST_KEYWORD.value()),
                jsonPath("$.message").value("검색 키워드는 공백을 입력할 수 없으며, 1글자 이상 입력해야 합니다.")
        ).andDo(
                document("post/findAllWithKeyword/fail/invalidKeyword",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("keyword").description("검색할 제목 키워드").optional()
                                        .attributes(getConstraints("constraints", "검색 키워드는 공백 제외 1글자 이상이어야 합니다."))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }
}
