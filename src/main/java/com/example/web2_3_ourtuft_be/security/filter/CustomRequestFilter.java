package com.example.web2_3_ourtuft_be.security.filter;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.UnauthorizedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.security.userdetails.UserDetailsImpl;
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
        // 현재 token 가져왔을떄 "Bearer " 문자열이 붙어서 들어옴 -> 인증 실패
        String token = request.getHeader("Authorization");
        if (token == null || request.getRequestURI().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = "";
        // token 이 null 일수도 있으므로 여기서 떼주었습니다. 변수명이나 조건문 위치 등등 수정 필요할듯
        if(token.startsWith("Bearer ")) {
            accessToken = token.substring(7);
        }
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

        UserDetailsImpl customUserDetails = new UserDetailsImpl(User.to(id, name, role));
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher pathMatcher = new AntPathMatcher();

        String[] paths = {
                "/api/v1/auth/**",
                "/api/v1/test/**"
        };

        String path = new UrlPathHelper().getPathWithinApplication(request);
        return Arrays.stream(paths)
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}
