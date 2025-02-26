package com.example.web2_3_ourtuft_be.websocket;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendMessage(
            SimpMessageHeaderAccessor headerAccessor, String message, Principal principal) {

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = headerAccessor.getFirstNativeHeader("roomId");
        System.out.println(principal);

        messagingTemplate.convertAndSend(
                "/server/room/" + roomId, new ChatRequest.Send(username, message));
    }
}
