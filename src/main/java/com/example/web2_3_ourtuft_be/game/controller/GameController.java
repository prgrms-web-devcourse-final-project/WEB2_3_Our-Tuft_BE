package com.example.web2_3_ourtuft_be.game.controller;

import com.example.web2_3_ourtuft_be.game.service.GameService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/start/{roomId}")
    public ResponseEntity<GlobalResponse<String>> startGame(@PathVariable Long roomId) {
        gameService.gameSet(roomId);
        return ResponseEntity.ok(GlobalResponse.success("Game started"));
    }
}
