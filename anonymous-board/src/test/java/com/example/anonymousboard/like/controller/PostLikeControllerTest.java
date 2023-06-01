package com.example.anonymousboard.like.controller;

import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.like.dto.PostLikeFlipResponse;
import com.example.anonymousboard.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class PostLikeControllerTest extends ControllerTest {

    @DisplayName("특정 게시글을 좋아요 하게 되면 200을 반환한다.")
    @Test
    void likePost_newLike() throws Exception {
        // given
        PostLikeFlipResponse postLikeFlipResponse = new PostLikeFlipResponse(true);
        given(postLikeService.flipPostLike(any(), any())).willReturn(postLikeFlipResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/like", 1L)
                .header(AUTHORIZATION, "any"));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.like").value(true)
        ).andDo(
                document("postLike/newLike/success",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("postId").description("게시글 아이디")
                        ),
                        responseFields(
                                fieldWithPath("like").type(JsonFieldType.BOOLEAN)
                                        .description("좋아요 누르기(true)/취소하기(false)")
                        )
                )

        );
    }

    @DisplayName("이미 좋아요를 누른 게시글을 좋아요 하게 되면 200을 반환한다.")
    @Test
    void likePost_removeLike() throws Exception {
        // given
        PostLikeFlipResponse postLikeFlipResponse = new PostLikeFlipResponse(false);
        given(postLikeService.flipPostLike(any(), any())).willReturn(postLikeFlipResponse);

        // when
        ResultActions result = mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/like", 1L)
                .header(AUTHORIZATION, "any"));

        // then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.like").value(false)
        ).andDo(
                document("postLike/removeLike/success",
                        getDocumentRequest(),
                        getDocumentResponse()
                )
        );
    }
}