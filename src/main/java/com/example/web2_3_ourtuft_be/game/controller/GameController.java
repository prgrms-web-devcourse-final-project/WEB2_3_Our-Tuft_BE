package com.example.web2_3_ourtuft_be.game.controller;

import com.example.web2_3_ourtuft_be.game.dto.*;
import com.example.web2_3_ourtuft_be.game.service.GameService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @GetMapping("/api/v1/game/{roomId}/scores")
    @Operation(summary = "플레이어의 점수 조회", description = "게임 종료 후 플레이어 들의 점수를 정렬 후 가져오는 API 입니다")
    public ResponseEntity<GlobalResponse<List<GameResponse.Scores>>> getGameScores(
            @PathVariable String roomId) {
        return ResponseEntity.ok(GlobalResponse.success(gameService.getGameScores(roomId)));
    }
}
