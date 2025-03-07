package com.example.web2_3_ourtuft_be.websocket.controller;

import com.example.web2_3_ourtuft_be.websocket.service.WSRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WSRoomController {

    private final WSRoomService wsRoomService;

    @MessageMapping("/room/{roomId}/event")
    @SendTo("/topic/room/{roomId}")
    public void handleRoomEvent(
            @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor,
            String event) {
        wsRoomService.handleRoomEvent(headerAccessor, roomId, event);
    }

    @SubscribeMapping("/room/{roomId}")
    public void handleRoomSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        wsRoomService.addPlayer(headerAccessor, roomId);
    }
}
