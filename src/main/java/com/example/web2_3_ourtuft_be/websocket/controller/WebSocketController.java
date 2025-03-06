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
    public void processMessage(
            @DestinationVariable Long roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String message) {
        webSocketService.processRoomMessage(headerAccessor, roomId, message);
    }

    @SubscribeMapping("/room/{roomId}")
    public void handleRoomSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        webSocketService.handleRoomSubscribe(headerAccessor, roomId);
    }

    @MessageMapping("/gameRoom/{roomId}")
    @SendTo("/topic/gameRoom/{roomId}")
    public void processGameRoom(
            @DestinationVariable Long roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String message) {
        webSocketService.processGameRoom(headerAccessor, roomId, message);
    }

    @SubscribeMapping("/gameRoom/{roomId}")
    public void handleGameRoomSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {}

    // /app/lobby 로 메세지 보내면, /topic/lobby 구독중인 모든 클라이언트에게 전달
    @MessageMapping("/lobby")
    @SendTo("/topic/lobby")
    public WebSocketResponse.Send sendMessageToLobby(
            SimpMessageHeaderAccessor headerAccessor, String message) {
        return webSocketService.sendMessageToRoom(headerAccessor, message);
    }
}
