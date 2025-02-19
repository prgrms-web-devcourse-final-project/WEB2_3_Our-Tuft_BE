package com.example.web2_3_ourtuft_be.global.config;

import com.example.web2_3_ourtuft_be.global.interceptor.WebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final WebSocketHandshakeInterceptor handshakeInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    // /topic/ 으로 시작하는 경로는 메시지 브로커에서 처리
    registry.enableSimpleBroker("/topic");

    // 클라이언트가 메시지를 전송할 경로
    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws-stomp").setAllowedOriginPatterns("*");
  }
}
