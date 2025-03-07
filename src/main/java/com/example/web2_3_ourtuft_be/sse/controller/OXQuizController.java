package com.example.web2_3_ourtuft_be.sse.controller;

import com.example.web2_3_ourtuft_be.game.service.OXGameService;
import com.example.web2_3_ourtuft_be.user.entity.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/ox-game")
@Tag(name = "OXGame", description = "OX 퀴즈 게임")
public class OXQuizController {
    private final OXGameService oxGameService;

    public OXQuizController(OXGameService oxGameService) {
        this.oxGameService = oxGameService;
    }

    // OX 퀴즈 SSE 구독 (클라에서 연결 요청)
    @GetMapping("/subscribe/{roomId}")
    public SseEmitter subscribe(
            @PathVariable String roomId, @AuthenticationPrincipal(expression = "user") User user) {
        return oxGameService.subscribeToRoom(roomId, user.getId());
    }
}
