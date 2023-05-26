package com.example.anonymousboard.support.token;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

public class JwtTokenExtractor {

    private static final String BEARER_PREFIX = "Bearer";

    public static String extractAccessToken(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader.startsWith(BEARER_PREFIX)) {
            return authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        }
        return null;
    }
}
