package com.example.web2_3_ourtuft_be.security.handler;

import com.example.web2_3_ourtuft_be.auth.dto.CustomOAuth2User;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import com.example.web2_3_ourtuft_be.user.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        String accessToken =
                jwtUtil.createJwt(
                        "access", user.getId(), user.getNickname(), user.getRole(), 86400000L);
        String refreshToken =
                jwtUtil.createJwt(
                        "refresh", user.getId(), user.getNickname(), user.getRole(), 86400000L);

        response.addCookie(jwtUtil.createCookie("refresh", refreshToken));

        jwtUtil.saveRefreshTokenInRedis(refreshToken);

        String SUCCESS_TARGET_URL = "https://hiq-lounge.vercel.app/login#token=";
        String targetUrl = SUCCESS_TARGET_URL + accessToken;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
