package com.example.web2_3_ourtuft_be.auth.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.UnauthorizedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public Void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshByCookie(request);

        if (refreshToken == null)
            throw new UnauthorizedException(UnauthorizedMessages.NOT_FOUND_REFRESH_TOKEN);
        if (jwtUtil.isExpired(refreshToken))
            throw new UnauthorizedException(UnauthorizedMessages.REFRESH_TOKEN_EXPIRED);
        if (!jwtUtil.getCategory(refreshToken).equals("refresh"))
            throw new UnauthorizedException(UnauthorizedMessages.INVALID_REFRESH_TOKEN);

        String refreshTokenInRedis =
                jwtUtil.getRefreshTokenInRedis(jwtUtil.getUserId(refreshToken));

        if (!refreshTokenInRedis.equals(refreshToken))
            throw new UnauthorizedException(UnauthorizedMessages.INVALID_REFRESH_TOKEN);

        Long userId = jwtUtil.getUserId(refreshTokenInRedis);
        String role = jwtUtil.getRole(refreshTokenInRedis);

        String newAccessToken = jwtUtil.createJwt("access", userId, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", userId, role, 86400000L);

        jwtUtil.saveRefreshTokenInRedis(newRefreshToken);

        response.setHeader("Authorization", newAccessToken);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addCookie(jwtUtil.createCookie("refresh", newRefreshToken));

        return null;
    }

    private String getRefreshByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
