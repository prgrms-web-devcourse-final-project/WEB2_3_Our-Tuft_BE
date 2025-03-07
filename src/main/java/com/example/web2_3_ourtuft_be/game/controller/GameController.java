package com.example.web2_3_ourtuft_be.game.controller;

import com.example.web2_3_ourtuft_be.game.dto.OXResponseDto;
import com.example.web2_3_ourtuft_be.game.dto.OXSubmitRequestDto;
import com.example.web2_3_ourtuft_be.game.service.GameService;
import com.example.web2_3_ourtuft_be.game.service.OXQuizService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final OXQuizService oxQuizService;

    //
    //    @PostMapping("/start/{roomId}")
    //    public ResponseEntity<GlobalResponse<String>> startGame(@PathVariable Long roomId) {
    //        gameService.gameSet(roomId);
    //
    //        return ResponseEntity.ok(GlobalResponse.success("Game started"));
    //    }

    @Operation(summary = "OX 정답 제출 API", description = "사용자가 O, X를 입력하여 정답 여부를 받습니다.")
    @PostMapping("/ox/submit")
    public ResponseEntity<OXResponseDto> submitAnswer(@RequestBody OXSubmitRequestDto requestDto) {

        OXResponseDto response =
                oxQuizService.checkAnswer(
                        requestDto.getRoomId(),
                        requestDto.getUserId(),
                        requestDto.getRound(),
                        requestDto.getAnswer());

        return ResponseEntity.ok(response);
    }
}
