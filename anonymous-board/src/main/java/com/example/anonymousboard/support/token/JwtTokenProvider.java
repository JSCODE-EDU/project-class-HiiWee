package com.example.anonymousboard.support.token;

import static com.example.anonymousboard.auth.exception.AuthErrorCode.EXPIRED_JWT;
import static com.example.anonymousboard.auth.exception.AuthErrorCode.INVALID_SIGNATURE;
import static com.example.anonymousboard.auth.exception.AuthErrorCode.MALFORMED_JWT;
import static com.example.anonymousboard.auth.exception.AuthErrorCode.UNSUPPORTED_JWT;

import com.example.anonymousboard.auth.dto.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORIZATION_ID = "id";

    private final Key signingKey;
    private final long validityInMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret-key}") final String signingKey,
                            @Value("${security.jwt.token.expire-length.access}") final long validityInMilliseconds) {
        byte[] keyBytes = signingKey.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
    }

    public String createAccessToken(final Long id) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()
                .claim(AUTHORIZATION_ID, id)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey)
                .compact();
    }

    public AuthInfo getParsedAuthInfo(final String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            return AuthInfo.from(claimsJws.getBody().get(AUTHORIZATION_ID));
        } catch (ExpiredJwtException e) {
            return AuthInfo.from(e.getClaims().get(AUTHORIZATION_ID));
        }
    }

    public void validateToken(final String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token : {}", token);
            throw new JwtException(String.format("지원하지 않는 JWT 토큰 형식:%d", UNSUPPORTED_JWT.value()));
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token : {}", token);
            throw new JwtException(String.format("토큰 기한 만료:%d", EXPIRED_JWT.value()));
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token : {}", token);
            throw new JwtException(String.format("유효하지 않은 JWT 토큰:%d", MALFORMED_JWT.value()));
        } catch (SignatureException e) {
            log.info("Invalid JWT signature : {}", token);
            throw new JwtException(String.format("잘못된 JWT 시그니처:%d", INVALID_SIGNATURE.value()));
        }
    }
}
