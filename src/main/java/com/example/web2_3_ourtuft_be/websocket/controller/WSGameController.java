package com.example.web2_3_ourtuft_be.websocket.controller;

import com.example.web2_3_ourtuft_be.websocket.service.WSGameService;
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
public class WSGameController {

    private final WSGameService wsGameService;
    private final WebSocketService webSocketService;

    @SubscribeMapping("/game/{roomId}")
    public void handleGameSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {

        wsGameService.gameSetting(roomId, headerAccessor);
        wsGameService.addPlayer(headerAccessor, roomId);
    }

    @MessageMapping("/game/{roomId}/speed")
    @SendTo("/topic/game/{roomId}")
    public void processGameMessage(
            @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String message) {
        webSocketService.sendGameMessage(roomId, webSocketService.getUsernameFromSession(headerAccessor), message);
        wsGameService.submitAnswer(roomId, headerAccessor, message);
    }

    @MessageMapping("/game/{roomId}/ox")
    public void processOXGameMessage(
            @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String message) {
        wsGameService.submitOXAnswer(roomId, headerAccessor, message);
    }
}
