package com.example.anonymousboard.support;

import static com.example.anonymousboard.auth.exception.AuthErrorCode.INVALID_HEADER_FORMAT;
import static com.example.anonymousboard.auth.exception.AuthErrorCode.NO_AUTHORIZATION_HEADER;

import com.example.anonymousboard.support.token.JwtTokenExtractor;
import com.example.anonymousboard.support.token.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthInterceptor(final JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
                             final Object handler) {
        if (CorsUtils.isPreFlightRequest(request) || isGetRequest(request)) {
            return true;
        }
        if (notExistHeader(request)) {
            throw new JwtException(String.format("인증을 위한 헤더를 찾을 수 없습니다.:%d", NO_AUTHORIZATION_HEADER.value()));
        }
        if (JwtTokenExtractor.extractAccessToken(request) == null) {
            throw new JwtException(String.format("헤더의 포멧이 일치하지 않습니다.:%d", INVALID_HEADER_FORMAT.value()));
        }
        jwtTokenProvider.validateToken(JwtTokenExtractor.extractAccessToken(request));

        return true;
    }

    private boolean isGetRequest(final HttpServletRequest request) {
        // TODO: 리팩토링 -> URL 하드코딩
        if (request.getRequestURI().equalsIgnoreCase("/members/me")) {
            return false;
        }
        return request.getMethod().equalsIgnoreCase(HttpMethod.GET.name());
    }

    private boolean notExistHeader(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return Objects.isNull(authorizationHeader);
    }
}
