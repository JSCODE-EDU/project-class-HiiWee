package com.example.anonymousboard.util;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import com.example.anonymousboard.auth.controller.AuthController;
import com.example.anonymousboard.auth.service.AuthService;
import com.example.anonymousboard.member.controller.MemberController;
import com.example.anonymousboard.member.service.MemberService;
import com.example.anonymousboard.post.controller.PostController;
import com.example.anonymousboard.post.service.PostService;
import com.example.anonymousboard.support.AuthInterceptor;
import com.example.anonymousboard.support.token.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest({
        AuthController.class,
        MemberController.class,
        PostController.class
})
@ExtendWith(RestDocumentationExtension.class)
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthInterceptor authInterceptor;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected PostService postService;


    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();

        given(authInterceptor.preHandle(any(), any(), any()))
                .willReturn(true);
    }
}
