package com.example.anonymousboard.comment.controller;

import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentResponse;
import static com.example.anonymousboard.util.apidocs.DocumentFormatGenerator.getConstraints;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.advice.CommonErrorCode;
import com.example.anonymousboard.comment.dto.CommentSaveRequest;
import com.example.anonymousboard.comment.dto.CommentSaveResponse;
import com.example.anonymousboard.comment.exception.CommentErrorCode;
import com.example.anonymousboard.comment.exception.InvalidCommentException;
import com.example.anonymousboard.member.exception.MemberErrorCode;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.post.exception.PostErrorCode;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.util.ControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class CommentControllerTest extends ControllerTest {

    CommentSaveRequest commentSaveRequest;

    @BeforeEach
    void setUp() {
        commentSaveRequest = new CommentSaveRequest("댓글 입니다.");
    }

    @DisplayName("댓글을 작성하면 201을 반환한다.")
    @Test
    void addComment() throws Exception {
        // given
        CommentSaveResponse commentSaveResponse = new CommentSaveResponse(1L);
        given(commentService.addComment(any(), any(), any())).willReturn(commentSaveResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/comments", 1L)
                .header(AUTHORIZATION, "any")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentSaveRequest)));

        // then
        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.savedId").value(1)
        ).andDo(
                document("comment/create/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                                        .attributes(getConstraints("constraints", "댓글은 1자 이상 50자 이하까지 작성할 수 있습니다"))
                        ),
                        responseFields(
                                fieldWithPath("savedId").type(JsonFieldType.NUMBER).description("저장된 댓글 아이디")
                        )
                )
        );
    }

    @DisplayName("회원이 존재하지 않는다면 404를 반환한다.")
    @Test
    void addComment_exception_notFoundMember() throws Exception {
        // given
        given(commentService.addComment(any(), any(), any())).willThrow(new MemberNotFoundException());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/comments", 1L)
                .header(AUTHORIZATION, "any")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentSaveRequest)));

        // then
        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").value("회원을 찾을 수 없습니다."),
                jsonPath("$.errorCode").value(MemberErrorCode.MEMBER_NOT_FOUND.value())
        ).andDo(
                document("comment/create/fail/notFoundMember",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                                        .attributes(getConstraints("constraints", "댓글은 1자 이상 50자 이하까지 작성할 수 있습니다"))
                        ),
                        responseFields(
                                fieldWithPath("message").type(JsonFieldType.STRING).description("실패 메시지"),
                                fieldWithPath("errorCode").type(JsonFieldType.NUMBER).description("에러 코드")
                        )
                )
        );
    }

    @DisplayName("게시글이 존재하지 않는다면 404를 반환한다.")
    @Test
    void addComment_exception_notFoundPost() throws Exception {
        // given
        given(commentService.addComment(any(), any(), any())).willThrow(new PostNotFoundException());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/comments", 1L)
                .header(AUTHORIZATION, "any")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentSaveRequest)));

        // then
        result.andExpectAll(
                status().isNotFound(),
                jsonPath("$.message").value("게시글을 찾을 수 없습니다."),
                jsonPath("$.errorCode").value(PostErrorCode.POST_NOT_FOUND.value())
        ).andDo(
                document("comment/create/fail/notFoundPost",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("댓글의 길이가 50을 넘는다면 400을 반환한다.")
    @Test
    void addComment_exception_invalidCommentLength() throws Exception {
        // given
        given(commentService.addComment(any(), any(), any())).willThrow(new InvalidCommentException());

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/comments", 1L)
                .header(AUTHORIZATION, "any")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(commentSaveRequest)));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("댓글은 1자 이상 50자 이하까지 작성할 수 있습니다"),
                jsonPath("$.errorCode").value(CommentErrorCode.INVALID_COMMENT.value())
        ).andDo(
                document("comment/create/fail/invalidCommentLength",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }

    @DisplayName("비어있는 댓글을 작성하면 400을 반환한다.")
    @Test
    void addComment_exception_blankComment() throws Exception {
        // given
        CommentSaveResponse commentSaveResponse = new CommentSaveResponse(1L);
        CommentSaveRequest blankCommentRequest = new CommentSaveRequest("    ");
        given(commentService.addComment(any(), any(), any())).willReturn(commentSaveResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/comments", 1L)
                .header(AUTHORIZATION, "any")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(blankCommentRequest)));

        // then
        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").value("댓글을 반드시 입력해야 합니다."),
                jsonPath("$.errorCode").value(CommonErrorCode.METHOD_ARGUMENT_NOT_VALID.value())
        ).andDo(
                document("comment/create/fail/blankComment",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }
}