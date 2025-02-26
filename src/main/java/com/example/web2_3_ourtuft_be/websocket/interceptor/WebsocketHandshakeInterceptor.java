package com.example.web2_3_ourtuft_be.websocket.interceptor;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.UnauthorizedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
public class WebsocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes)
            throws Exception {
        URI uri = request.getURI();

        String token =
                UriComponentsBuilder.fromUri(uri)
                        .build()
                        .getQueryParams()
                        .getFirst("authorization");

        if (token != null && validate(token)) {
            String username = jwtUtil.getUserName(token);
            attributes.put("username", username);
            Long userId = jwtUtil.getUserId(token);
            attributes.put("userId", userId);

            return true;
        }

        response.getBody().write(1);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
        System.out.println("afterHandshake");
        if (exception != null) {
            System.out.println(exception);
        }
    }

    private boolean validate(String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(UnauthorizedMessages.ACCESS_TOKEN_EXPIRED);
        }

        return "access".equals(jwtUtil.getCategory(token));
    }
}
