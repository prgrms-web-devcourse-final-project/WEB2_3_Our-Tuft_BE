package com.example.web2_3_ourtuft_be.sse.controller;

import com.example.web2_3_ourtuft_be.game.service.OXGameService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import com.example.web2_3_ourtuft_be.user.entity.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import io.swagger.v3.oas.annotations.tags.Tag;

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
    public SseEmitter subscribe(@PathVariable String roomId,
                                @AuthenticationPrincipal(expression = "user") User user) {
        return oxGameService.subscribeToRoom(roomId, user.getId());
    }

    // 퀴즈 시작 요청 (방장이 호출)
    @PostMapping("/start/{roomId}")
    public ResponseEntity<GlobalResponse<String>> startGame(@PathVariable String roomId) {
        oxGameService.startQuiz(roomId);
        return ResponseEntity.ok(GlobalResponse.success("게임 시작: " + roomId));
    }
}
