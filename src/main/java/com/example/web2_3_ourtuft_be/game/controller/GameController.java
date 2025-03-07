package com.example.web2_3_ourtuft_be.game.controller;

import com.example.web2_3_ourtuft_be.game.dto.OXFinishDto;
import com.example.web2_3_ourtuft_be.game.dto.OXQuizResponse;
import com.example.web2_3_ourtuft_be.game.dto.OXResponseDto;
import com.example.web2_3_ourtuft_be.game.dto.OXSubmitRequestDto;
import com.example.web2_3_ourtuft_be.game.service.OXQuizService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final OXQuizService oxQuizService;

    @Operation(summary = "OX 정답 제출 API", description = "사용자가 O, X를 입력하여 정답 여부를 받습니다.")
    @PostMapping("/ox/quiz/{roomId}/{round}")
    public ResponseEntity<OXResponseDto> submitAnswer(
            @RequestBody OXSubmitRequestDto requestDto,
            @PathVariable Long roomId,
            @PathVariable int round,
            @AuthenticationPrincipal(expression = "user") User user) {

        OXResponseDto response =
                oxQuizService.checkAnswer(
                        user.getId(), roomId, round, requestDto.getAnswer());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/ox/quiz/{roomId}/{round}")
    public ResponseEntity<GlobalResponse<OXQuizResponse>> getQuiz(
            @PathVariable Long roomId, @PathVariable int round) {
        OXQuizResponse response = oxQuizService.getQuiz(roomId, round);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    // 시작은 1번문제 조회 -> 1번문제, 현재 번호, 마지막 문제인지 아닌지 true/false

    @GetMapping("/ox/quiz/{roomId}/finish")
    public ResponseEntity<GlobalResponse<OXFinishDto>> finish(
            @PathVariable Long roomId, @AuthenticationPrincipal(expression = "user") User user) {

        OXFinishDto response = oxQuizService.finish(user.getId(), roomId);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
