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

    // channelInterceptor 를 사용해서 헤더에 토큰을 담아서 하려고 했지만
    // 연결 단계에서만 인증을 할거면 웹소켓 연결 - 토큰 인증 - STOMP 연결 이거보단
    // 토큰 인증 - 웹소켓 연결 - STOMP 연결이 낫다..?라고 생각해서 핸드셰이크부에 구현
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
        // url 파라미터에서 토큰 값 추출
        // 만약에 현재 인증 구조를 인증 - 웹소켓 - STOMP 순서에서
        // 웹소켓 - 인증 - STOMP 로 바꾼다면
        // STOMP의 CONNECT일 경우에 header 의 authorization 을 파밍하도록 수정 가능
        // 이 경우는 url 에 파라미터로 들어가지 않는다

        if (token != null && validate(token)) {
            String username = jwtUtil.getUserName(token);
            attributes.put("username", username);
            Long userId = jwtUtil.getUserId(token);
            attributes.put("userId", String.valueOf(userId));

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

    // 예외 넘어가게 할 방법을 찾는 중(콘솔에서는 뜬다)
    // 잘못된 접근인지, 토큰의 만료인지, 유효한 토큰이 아닌지
    // 예외처리방법 구상중
    private boolean validate(String token) {
        try {
            jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException(UnauthorizedMessages.ACCESS_TOKEN_EXPIRED);
        }

        return "access".equals(jwtUtil.getCategory(token));
    }
}
