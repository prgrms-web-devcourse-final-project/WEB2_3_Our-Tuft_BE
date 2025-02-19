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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class CustomRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = request.getHeader("Authorization");
        log.info(this.getClass().getName() + " access token: " + accessToken);

        if (accessToken == null || request.getRequestURI().equals("/api/v1/auth/reissue")) {
            filterChain.doFilter(request, response);
            return;
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
        String role = jwtUtil.getRole(accessToken);

        UserDetailsImpl customUserDetails = new UserDetailsImpl(User.to(id, role));
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
