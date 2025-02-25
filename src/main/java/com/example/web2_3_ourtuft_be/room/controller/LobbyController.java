package com.example.web2_3_ourtuft_be.room.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lobbies")
@Tag(name = "📬 Lobby", description = "로비 및 방 관련 API")
public class LobbyController {

    private final LobbyService lobbyService;

    @Operation(summary = "방 전체 조회 API", description = "로비에서 생성된 방을 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "방이 존재하지 않습니다."),
    })
    @GetMapping("/rooms")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto>>> viewAllRooms() {

        List<RoomResponseDto> responseList = lobbyService.getAllRooms();

        return ResponseEntity.ok(GlobalResponse.success(responseList));
    }

    @Operation(summary = "방 검색 API", description = "방 제목과 방ID 로 방을 검색합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "400", description = "검색 조건을 입력하세요."),
        @ApiResponse(responseCode = "404", description = "해당 ID의 방을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "404", description = "해당 이름을 포함하는 방을 찾을 수 없습니다.")
    })
    @GetMapping("/rooms/search")
    public ResponseEntity<GlobalResponse<List<RoomResponseDto>>> searchRoom(
            @RequestParam(required = false) String roomName,
            @RequestParam(required = false) Long roomId) {

        List<RoomResponseDto> responseList = lobbyService.searchRoom(roomName, roomId);

        return ResponseEntity.ok(GlobalResponse.success(responseList));
    }

    @Operation(summary = "방 생성 API", description = "게임 방을 생성합니다.")
    @PostMapping("/rooms")
    public ResponseEntity<GlobalResponse<RoomResponseDto>> createRoom(
            @Valid @RequestBody RoomRequestDto roomRequestDto
            //            @AuthenticationPrincipal UserDetails user) {
            ) {

        // RoomResponseDto response = lobbyService.createRoom(roomRequestDto, user.getUsername());
        RoomResponseDto response = lobbyService.createRoom(roomRequestDto);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
