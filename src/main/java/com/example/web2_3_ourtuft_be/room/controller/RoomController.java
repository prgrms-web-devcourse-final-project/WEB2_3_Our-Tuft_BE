package com.example.web2_3_ourtuft_be.room.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
@Tag(name = "📬 Room", description = "방 관련 API")
public class RoomController {

    private final RoomService roomService;
    private final ParticipantService participantService;
    private final RoomQuizService roomQuizService;

    @Operation(summary = "방 참가자 목록 조회", description = "현재 방의 참가자 목록을 조회합니다.")
    @GetMapping("/{roomId}/players")
    public ResponseEntity<GlobalResponse<RoomResponseDto.GetPlayersInRoom>> getPlayersInRoom(
            @PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(
                GlobalResponse.success(
                        RoomResponseDto.GetPlayersInRoom.of(
                                roomService.getHostIdByRoomId(roomId),
                                participantService.getPlayersInRoom(roomId))));
    }

    @Operation(summary = "게임에서 진행할 퀴즈세트 세팅", description = "퀴즈 목록 중 진행할 퀴즈 세트를 지정합니다. ")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @PutMapping("/{roomId}/quizzes/{quizSetId}")
    public ResponseEntity<GlobalResponse<String>> setQuizSet(
            @PathVariable("roomId") Long roomId, @PathVariable("quizSetId") Long quizSetId) {
        roomQuizService.setQuizSet(roomId, quizSetId);
        return ResponseEntity.ok(GlobalResponse.success("퀴즈 세트 지정 완료"));
    }

    @Operation(summary = "게임 방 유저 목록 조회 API", description = "게임 방의 플레이어 목록을 가져옵니다")
    @GetMapping("/{roomId}/game/players")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto.GetPlayerInGame>>>
            getGamePlayersInRoom(@PathVariable String roomId) {
        return ResponseEntity.ok(GlobalResponse.success(roomService.getPlayersInGame(roomId)));
    }
}
