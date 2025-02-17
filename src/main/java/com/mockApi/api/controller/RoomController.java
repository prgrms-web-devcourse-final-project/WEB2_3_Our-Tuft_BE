package com.mockApi.api.controller;

import com.mockApi.api.dto.ResponseMessageDto;
import com.mockApi.api.dto.RoomRequestDto;
import com.mockApi.api.dto.RoomResponseDto;
import com.mockApi.api.global.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "🕋 대기실", description = "대기실 관련 API")
public class RoomController {

  private final List<RoomResponseDto> rooms = new ArrayList<>();

  public RoomController() {
    rooms.add(new RoomResponseDto(1L, "스피드 퀴즈", "스피드 퀴즈입니다", false, 100, 8, 5, "user1"));
    rooms.add(new RoomResponseDto(2L, "OX 퀴즈", "OX 퀴즈입니다", true, 120, 8, 5, "user2"));
    rooms.add(new RoomResponseDto(3L, "캐치마인드", "캐치마인드 입니다", false, 180, 8, 5, "user3"));
  }

  @Operation(summary = "전체 대기실 목록 조회", description = "현재 모든 대기실을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/rooms")
  public ResponseEntity<GlobalResponse<List<RoomResponseDto>>> getRooms(
      @RequestParam(required = false) String gameType,
      @RequestParam(required = false) String roomId,
      @RequestParam(required = false) String roomName) {

    List<RoomResponseDto> filteredRooms =
        rooms.stream()
            .filter(room -> gameType == null || room.getGameType().equals(gameType))
            .filter(room -> roomId == null || room.getId().equals(Long.valueOf(roomId)))
            .filter(room -> roomName == null || room.getRoomName().contains(roomName))
            .toList();

    return ResponseEntity.ok().body(GlobalResponse.success(filteredRooms));
  }

  @Operation(summary = "대기실 입장", description = "특정 대기실에 입장합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PostMapping("/room/{roomId}")
  public ResponseEntity<GlobalResponse<?>> joinRoom(@PathVariable Long roomId) {

    RoomResponseDto mockRoom =
        new RoomResponseDto(roomId, "스피드 퀴즈", "스피드 퀴즈입니다", false, 100, 8, 5, "user1");

    return ResponseEntity.ok().body(GlobalResponse.success(mockRoom));
  }

  @Operation(summary = "대기실 생성", description = "대기실을 생성합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PostMapping("/room")
  public ResponseEntity<GlobalResponse<?>> createRoom(@RequestBody RoomRequestDto request) {
    String message = validateRoomRequest(request);
    if (message != null) {
      return ResponseEntity.badRequest()
          .body(GlobalResponse.failed(HttpStatus.BAD_REQUEST, new ResponseMessageDto(message)));
    }

    return ResponseEntity.ok().body(GlobalResponse.success(rooms));
  }

  private String validateRoomRequest(RoomRequestDto request) {

    if (!request.getRoomName().matches("^[a-zA-Z가-힣\\s]{4,20}$")) {
      return "방 제목은 한글 또는 영문, 최소 4자~최대 20자입니다.";
    }

    if (!request.isDisclosure()) {
      if (request.getRoomPassword() == null || !request.getRoomPassword().matches("^\\d{2,4}$")) {
        return "비공개 방의 경우, 2~4자리 숫자로 된 비밀번호를 입력해야 합니다.";
      }
    }

    if (request.getPeopleEntering() < 2 || request.getPeopleEntering() > 8) {
      return "입장 가능한 인원은 최소 2명~최대 8명까지 가능합니다.";
    }

    if (request.getGameType().equals("캐치마인드")) {
      if (request.getRound() < 5 || request.getRound() > 10) {
        return "캐치마인드는 최소 5라운드~최대 10라운드까지 설정할 수 있습니다.";
      }
    }

    return null;
  }
}
