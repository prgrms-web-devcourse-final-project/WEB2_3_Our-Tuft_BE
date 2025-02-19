package com.example.web2_3_ourtuft_be.security.handler;

import com.example.web2_3_ourtuft_be.global.exception.messages.AccessDeniedMessages;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 권한이 없는 사용자가 보호된 리소스에 접근하려고 할 때 처리하는 클래스 - Spring Security 의 'AccessDeniedHandler' 를 구현하여 동작 -
 * 사용자가 인증 (Authentication) 되었지만, 특정 리소스에 대한 권한이 없을 경우 403 Forbidden 응답 - 기본 HTML 응답 대신 정해진 응답 형식으로
 * 에러 메시지를 반환
 *
 * @author DoHyun
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        GlobalResponse<Object> errorResponse =
                GlobalResponse.fail(
                        new com.example.web2_3_ourtuft_be.global.exception.exceptions
                                .AccessDeniedException(AccessDeniedMessages.ADMIN_ONLY_ACCESS));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
