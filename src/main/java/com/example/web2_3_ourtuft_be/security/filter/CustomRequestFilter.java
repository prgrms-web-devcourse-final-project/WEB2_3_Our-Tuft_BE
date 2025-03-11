package com.example.web2_3_ourtuft_be.security.filter;

import com.example.web2_3_ourtuft_be.auth.dto.CustomOAuth2User;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.UnauthorizedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

@Slf4j
@RequiredArgsConstructor
public class CustomRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        // 인증이 필요 없는 URL은 바로 필터 체인을 진행
        if (authorizationHeader == null
                || authorizationHeader.isBlank()
                || request.getRequestURI().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 토큰이면 "Bearer "를 제거, 아니면 그대로 사용
        String accessToken =
                authorizationHeader.startsWith("Bearer ")
                        ? authorizationHeader.substring(7)
                        : authorizationHeader;

        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {
            response.setContentType("application/json; charset=UTF-8");

            GlobalResponse<Object> errorResponse =
                    GlobalResponse.fail(
                            new UnauthorizedException(UnauthorizedMessages.ACCESS_TOKEN_EXPIRED));

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), errorResponse);

            return;
        }
        if (!jwtUtil.getCategory(accessToken).equals("access")) {
            response.setContentType("application/json; charset=UTF-8");

            GlobalResponse<Object> errorResponse =
                    GlobalResponse.fail(
                            new UnauthorizedException(UnauthorizedMessages.INVALID_ACCESS_TOKEN));

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), errorResponse);

            return;
        }
        authenticate(accessToken);
        filterChain.doFilter(request, response);
    }

    /** JWT 에서 사용자 정보를 추출하여 Spring Security 의 인증(Authentication) 객체를 설정. */
    private void authenticate(String accessToken) {
        Long id = jwtUtil.getUserId(accessToken);
        String name = jwtUtil.getUserName(accessToken);
        String role = jwtUtil.getRole(accessToken);
        Map<String, Object> attributes = Collections.emptyMap();

        CustomOAuth2User oAuth2User = new CustomOAuth2User(User.to(id, name, role), attributes);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        oAuth2User, null, oAuth2User.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();

        String[] paths = {"/api/v1/auth/**", "/api/v1/test/**"};

        String path = new UrlPathHelper().getPathWithinApplication(request);
        return Arrays.stream(paths).anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
