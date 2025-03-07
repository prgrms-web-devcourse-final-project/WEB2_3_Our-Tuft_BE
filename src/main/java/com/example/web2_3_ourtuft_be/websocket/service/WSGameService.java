package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSGameService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendQuestion(String roomId, String question) {
        messagingTemplate.convertAndSend(
                "/topic/gameRoom/" + roomId, WebSocketResponse.SendQuestion.of(question));
    }

    public void sendSystemMessage(String roomId, String message) {
        messagingTemplate.convertAndSend(
                "/topic/gameRoom/" + roomId, WebSocketResponse.Send.of("SYSTEM", message));
    }

    public void sendSystemMessageToUser(String userId, String roomId, String message) {
        messagingTemplate.convertAndSendToUser(
                userId, "/topic/gameRoom/" + roomId, WebSocketResponse.Send.of("SYSTEM", message));
    }

    public void sendUserMessage(String roomId, String username, String message) {
        messagingTemplate.convertAndSend(
                "/topic/gameRoom/" + roomId, WebSocketResponse.Send.of(username, message));
    }
}
