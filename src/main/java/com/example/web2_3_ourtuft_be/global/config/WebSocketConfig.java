package com.example.web2_3_ourtuft_be.global.config;

import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import com.example.web2_3_ourtuft_be.websocket.interceptor.StompChannelInterceptor;
import com.example.web2_3_ourtuft_be.websocket.interceptor.WebsocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/server");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.setErrorHandler(new StompSubProtocolErrorHandler())
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new WebsocketHandshakeInterceptor(jwtUtil));
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new StompChannelInterceptor());
    }
}
