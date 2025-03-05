package com.example.web2_3_ourtuft_be.websocket.controller;

import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import com.example.web2_3_ourtuft_be.websocket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketService webSocketService;

    // 보내는 경로 예시) /app/room/1
    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public WebSocketResponse.Send processMessage(
            @DestinationVariable Long roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String message) {
        return webSocketService.processMessage(headerAccessor, roomId, message);
    }

    @SubscribeMapping("/room/{roomId}")
    public void handleRoomSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        webSocketService.handleRoomSubscribe(headerAccessor, roomId);
    }

    // /app/lobby 로 메세지 보내면, /topic/lobby 구독중인 모든 클라이언트에게 전달
    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public WebSocketResponse.Send sendMessageToLobby(
            SimpMessageHeaderAccessor headerAccessor, String message) {
        return webSocketService.sendMessageToRoom(headerAccessor, message);
    }
}
