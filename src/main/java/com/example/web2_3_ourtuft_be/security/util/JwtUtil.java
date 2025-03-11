package com.example.web2_3_ourtuft_be.security.util;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * JWT 관련 메서드를 제공하는 유틸리티 클래스 - JWT 생성, 파싱, 만료 여부 확인 등의 기능을 수행 - 사용자 정보(이메일, 역할, 카테고리)를 JWT 에서 추출 가능
 * - JWT 를 쿠키에 저장하는 기능도 포함
 *
 * @author DoHyun
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final RedisTemplate<String, Object> redisTemplate;
    private final long refreshTokenExpiration = 7 * 24 * 60 * 60;

    public JwtUtil(
            @Value("${spring.jwt.secret}") String secret,
            RedisTemplate<String, Object> redisTemplate) {
        key =
                new SecretKeySpec(
                        secret.getBytes(StandardCharsets.UTF_8),
                        Jwts.SIG.HS256.key().build().getAlgorithm());
        this.redisTemplate = redisTemplate;
    }

    public Long getUserId(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Long.class);
    }

    public String getUserName(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("name", String.class);
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    public String getCategory(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    public String createJwt(
            String category, Long userId, String name, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("userId", userId)
                .claim("name", name)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key)
                .compact();
    }

    public void saveRefreshTokenInRedis(String refreshToken) {
        redisTemplate
                .opsForValue()
                .set(
                        "RT:" + getUserId(refreshToken).toString(),
                        refreshToken,
                        refreshTokenExpiration,
                        TimeUnit.SECONDS);
    }

    public void deleteRefreshTokenInRedis(String key) {
        redisTemplate.delete(key);
    }

    public String getRefreshTokenInRedis(Long userId) {
        Object token = redisTemplate.opsForValue().get("RT:" + userId.toString());
        return token != null ? token.toString() : null;
    }

    public Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
