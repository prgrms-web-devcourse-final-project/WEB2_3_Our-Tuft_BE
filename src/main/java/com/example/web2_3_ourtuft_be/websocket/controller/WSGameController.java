package com.example.web2_3_ourtuft_be.websocket.controller;

import com.example.web2_3_ourtuft_be.websocket.service.WSGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WSGameController {

    private final WSGameService wsGameService;

    @SubscribeMapping("/game/{roomId}")
    public void handleGameSubscribe(
            @DestinationVariable String roomId, SimpMessageHeaderAccessor headerAccessor) {
        wsGameService.gameSetting(roomId, headerAccessor);
    }
}
