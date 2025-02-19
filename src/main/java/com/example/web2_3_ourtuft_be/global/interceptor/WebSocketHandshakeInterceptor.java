package com.example.web2_3_ourtuft_be.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Map<String, Object> attributes)
      throws Exception {

    if (request instanceof HttpServletRequest) {
      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
      String token = servletRequest.getServletRequest().getHeader("access");

      if (token == null) {
        return false;
      }

      // JWT 적용 시 토큰의 nickname 을 읽을 예정
      String nickname = token;
      attributes.put("nickname", nickname);

      return true;
    }

    return false;
  }

  @Override
  public void afterHandshake(
      ServerHttpRequest request,
      ServerHttpResponse response,
      WebSocketHandler wsHandler,
      Exception exception) {}
}
