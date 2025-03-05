package com.example.web2_3_ourtuft_be.websocket.controller;

import com.example.web2_3_ourtuft_be.websocket.dto.LobbySubscribeResponse;
import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import com.example.web2_3_ourtuft_be.websocket.service.LobbySocketService;
import com.example.web2_3_ourtuft_be.websocket.service.RoomSocketService;
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
    private final RoomSocketService roomSocketService;
    private final LobbySocketService lobbySocketService;


    // 보내는 경로 예시) /app/room/1
    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public WebSocketResponse.Send sendMessageToRoom(
            @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String message) {
        return webSocketService.sendMessageToRoom(headerAccessor, message);
    }

    @SubscribeMapping("/room/lobby")
    public LobbySubscribeResponse handleLobbySubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {

        return lobbySocketService.enterLobby(headerAccessor);

    }

    @SubscribeMapping("/room/{roomId}")
    public void handleRoomSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        RoomSocketService.enterRoom(headerAccessor, roomId);
    }


}
