package com.example.web2_3_ourtuft_be.room.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lobbies")
@Tag(name = "📬 Lobby", description = "로비 및 방 관련 API")
public class LobbyController {
    private final LobbyService lobbyService;

    private final RoomQuizService roomQuizService;

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
            @Valid @RequestBody RoomRequestDto roomRequestDto,
            @AuthenticationPrincipal(expression = "user") User user) {

        RoomResponseDto response = lobbyService.createRoom(roomRequestDto, user.getId());

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "방 설정 변경 API", description = "방 설정을 변경합니다.")
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<GlobalResponse<RoomResponseDto>> updateRoomSettings(
            @PathVariable Long roomId,
            @Valid @RequestBody RoomRequestDto roomRequestDto,
            @AuthenticationPrincipal(expression = "user") User user) {

        RoomResponseDto response =
                lobbyService.updateRoomSettings(roomId, user.getId(), roomRequestDto);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "방장 변경 API", description = "방장을 변경합니다.")
    @PutMapping("/rooms/{roomId}/host")
    public ResponseEntity<GlobalResponse<String>> changeRoomHost(
            @PathVariable Long roomId, @RequestParam Long newHostId) {

        lobbyService.changeRoomHost(roomId, newHostId);

        return ResponseEntity.ok(GlobalResponse.success("방장 변경 성공"));
    }

    @Operation(summary = "방 나가기 API", description = "사용자가 특정 방을 나갑니다. 잔여인원이 0명이 되면 방이 삭제됩니다.")
    @DeleteMapping("/rooms/{roomId}/participant/{userId}")
    public ResponseEntity<GlobalResponse<String>> leaveRoom(
            @PathVariable Long roomId, @PathVariable Long userId) {

        GlobalResponse<String> response = lobbyService.leaveRoom(roomId, userId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게임에서 진행할 퀴즈세트 세팅", description = "퀴즈목록중 진행할 퀴즈세트를 지정합니다. ")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @PutMapping("/rooms/{roomId}/quizzes/{quizSetId}")
    public ResponseEntity<GlobalResponse<String>> setQuizSet(
            @PathVariable("roomId") Long roomId, @PathVariable("quizSetId") Long quizSetId) {

        roomQuizService.setQuizSet(roomId, quizSetId);

        return ResponseEntity.ok(GlobalResponse.success("퀴즈세트 저장"));
    }
}
