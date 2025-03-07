package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // 핸드셰이크에서 Principal 객체에 회원 정보를 담으려고 했지만 Null 값이 넘어오는 문제를 아직 해결 X
    // 핸드셰이크 JWT 인증단계에서 attributes 에 키벨류로 담아뒀다
    public String getUserIdFromSession(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("userId");
    }

    // 핸드셰이크에서 Principal 객체에 회원 정보를 담으려고 했지만 Null 값이 넘어오는 문제를 아직 해결 X
    // 핸드셰이크 JWT 인증단계에서 attributes 에 키벨류로 담아뒀다
    public String getUsernameFromSession(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }

    public void sendMessage(SimpMessageHeaderAccessor headerAccessor, Long roomId, String message) {
        String username = getUsernameFromSession(headerAccessor);

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId, WebSocketResponse.Send.of(username, message));
    }

    public void sendSystemMessageToUser(String userId, Long roomId, String message) {
        messagingTemplate.convertAndSendToUser(
                userId, "/topic/room/" + roomId, WebSocketResponse.Send.of(userId, message));
    }

    public void sendEvent(String roomId, String event) {
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId, WebSocketResponse.SendEvent.of(event));
    }

    public void sendGameEvent(String roomId, String event) {
        messagingTemplate.convertAndSend(
                "/topic/game/" + roomId, WebSocketResponse.SendEvent.of(event));
    }

    public void sendSystemMessage(String roomId, String message) {
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId, WebSocketResponse.Send.of("SYSTEM", message));
    }
}
