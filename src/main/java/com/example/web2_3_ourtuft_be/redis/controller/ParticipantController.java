package com.example.web2_3_ourtuft_be.redis.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.redis.dto.ParticipantDto;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @Operation(summary = "참여자 목록 조회", description = "참여자 목록을 조회합니다. ( Room, Lobby 공통)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/{roomid}")
    public ResponseEntity<GlobalResponse<List<ParticipantDto>>> registQuizset(
            @PathVariable("roomid") String roomId) {

        List<ParticipantDto> participants = participantService.getParticipants(roomId);

        return ResponseEntity.ok(GlobalResponse.success(participants));
    }
}
