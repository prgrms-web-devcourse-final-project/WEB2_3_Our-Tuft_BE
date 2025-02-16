package com.mockApi.api.controller;

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
}