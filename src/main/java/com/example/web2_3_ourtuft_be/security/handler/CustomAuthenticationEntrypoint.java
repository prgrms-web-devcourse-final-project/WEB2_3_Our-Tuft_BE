package com.example.web2_3_ourtuft_be.security.handler;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.UnauthorizedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntrypoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        GlobalResponse<Object> errorResponse =
                GlobalResponse.fail(
                        new UnauthorizedException(UnauthorizedMessages.AUTHENTICATION_REQUIRED));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
