package com.example.web2_3_ourtuft_be.lobby.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.lobby.service.LobbyService;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LobbyController {

    private final LobbyService lobbyService;

    @Operation(summary = "방 전체 조회", description = "로비에서 생성된 방을 조회하는 API입니다.")
    @GetMapping("/lobby")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto>>> viewAllRooms() {

        List<RoomResponseDto> responseList = lobbyService.getAllRooms();

        return ResponseEntity.ok(GlobalResponse.success(responseList));
    }

    @Operation(summary = "방 검색", description = "방 제목과 ID로 검색하는 API입니다.")
    @GetMapping("/room")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto>>> searchRoom(
            @RequestParam(required = false) String roomName,
            @RequestParam(required = false) Long roomId) {

        List<RoomResponseDto> responseList = lobbyService.searchRoom(roomName, roomId);

        return ResponseEntity.ok(GlobalResponse.success(responseList));
    }
}
