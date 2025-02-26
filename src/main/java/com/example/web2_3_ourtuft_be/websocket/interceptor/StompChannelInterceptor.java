package com.example.web2_3_ourtuft_be.websocket.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class StompChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String username = (String) accessor.getSessionAttributes().get("username");

            if (username != null) {
                UsernamePasswordAuthenticationToken principal =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                accessor.setUser(principal);
                accessor.setLeaveMutable(true);
            }
        }
        return message;
    }
}
