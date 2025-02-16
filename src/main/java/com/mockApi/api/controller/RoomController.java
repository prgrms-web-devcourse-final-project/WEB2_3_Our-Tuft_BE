package com.mockApi.api.controller;

import com.mockApi.api.dto.ResponseMessageDto;
import com.mockApi.api.dto.RoomRequestDto;
import com.mockApi.api.dto.RoomResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RoomController {

    private final List<RoomResponseDto> rooms = new ArrayList<>();

    public RoomController() {
        rooms.add(new RoomResponseDto(1L, "스피드 퀴즈", "스피드 퀴즈입니다", false, 100, 8, 5, "user1"));
        rooms.add(new RoomResponseDto(2L, "OX 퀴즈", "OX 퀴즈입니다", true, 120, 8, 5, "user2"));
        rooms.add(new RoomResponseDto(3L, "캐치마인드", "캐치마인드 입니다", false, 180, 8, 5, "user3"));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponseDto>> getRooms(
            @RequestParam(required = false) String gameType,
            @RequestParam(required = false) String roomId,
            @RequestParam(required = false) String roomName) {

        List<RoomResponseDto> filteredRooms = rooms.stream()
                .filter(room -> gameType == null || room.getGameType().equals(gameType))
                .filter(room -> roomId == null || room.getId().equals(Long.valueOf(roomId)))
                .filter(room -> roomName == null || room.getRoomName().contains(roomName))
                .toList();

        return ResponseEntity.ok(filteredRooms);
    }

    @PostMapping("/room/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable Long roomId) {

        RoomResponseDto mockRoom = new RoomResponseDto(roomId,"스피드 퀴즈", "스피드 퀴즈입니다", false, 100, 8, 5, "user1");

        return ResponseEntity.ok(mockRoom);
    }

    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequestDto request) {
        String message = validateRoomRequest(request);
        if (message != null) {
            return ResponseEntity.badRequest().body(new ResponseMessageDto(message));
        }

        return ResponseEntity.ok(new ResponseMessageDto("방 생성 성공"));
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