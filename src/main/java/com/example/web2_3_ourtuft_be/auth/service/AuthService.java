package com.example.web2_3_ourtuft_be.auth.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.UnauthorizedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public void reissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = getRefreshByCookie(request).getValue();

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
        String nickname = jwtUtil.getUserName(refreshTokenInRedis);

        String newAccessToken = jwtUtil.createJwt("access", userId, nickname, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", userId, nickname, role, 86400000L);

        jwtUtil.saveRefreshTokenInRedis(newRefreshToken);

        response.setHeader("Authorization", newAccessToken);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addCookie(jwtUtil.createCookie("refresh", newRefreshToken));
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshToken = getRefreshByCookie(request);

        if (!jwtUtil.getCategory(refreshToken.getValue()).equals("refresh"))
            throw new UnauthorizedException(UnauthorizedMessages.INVALID_REFRESH_TOKEN);

        jwtUtil.deleteRefreshTokenInRedis("RT:" + jwtUtil.getUserId(refreshToken.getValue()));

        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");
        response.addCookie(refreshToken);
    }

    private Cookie getRefreshByCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) throw new UnauthorizedException(UnauthorizedMessages.NOT_FOUND_COOKIE);

        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                return cookie;
            }
        }

        throw new UnauthorizedException(UnauthorizedMessages.NOT_FOUND_REFRESH_TOKEN);
    }
}
