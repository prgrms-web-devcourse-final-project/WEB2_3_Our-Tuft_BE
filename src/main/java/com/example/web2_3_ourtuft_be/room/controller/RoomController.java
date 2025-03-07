package com.example.web2_3_ourtuft_be.room.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
@Tag(name = "📬 Room", description = "방 관련 API")
public class RoomController {

    private final RoomService roomService;
    private final ParticipantService participantService;

    @Operation(summary = "방 참가자 목록 조회", description = "현재 방의 참가자 목록을 조회합니다.")
    @GetMapping("/{roomId}/players")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto.GetPlayerInRoom>>> getPlayersInRoom(
            @PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(
                GlobalResponse.success(participantService.getPlayersInRoom(roomId)));
    }
}
