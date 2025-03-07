package com.example.web2_3_ourtuft_be.room.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.RoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/room")
public class RoomController {

    private final RoomService roomService;
    private final ParticipantService participantService;

    @GetMapping("/{roomId}/players")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto.GetPlayerInRoom>>> getPlayersInRoom(
            @PathVariable("roomId") String roomId) {
        return ResponseEntity.ok(
                GlobalResponse.success(participantService.getPlayersInRoom(roomId)));
    }
}
