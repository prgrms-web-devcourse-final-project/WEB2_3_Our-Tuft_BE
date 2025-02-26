package com.example.web2_3_ourtuft_be.websocket;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/send")
    public void send(
            SimpMessageHeaderAccessor headerAccessor, String message, Principal principal) {
        chatService.sendMessage(headerAccessor, message, principal);
    }
}
