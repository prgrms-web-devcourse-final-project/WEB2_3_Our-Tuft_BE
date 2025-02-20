package com.example.web2_3_ourtuft_be.lobby.controller;

import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.lobby.service.LobbyService;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class lobbyController {

    private final LobbyService lobbyService;
    private final RoomRepository roomRepository;

    @Operation(summary = "방 검색", description = "방 제목과 ID로 검색하는 API입니다.")
    @GetMapping("/room")
    public ResponseEntity<?> searchRoom(
            @RequestParam(required = false) String roomName,
            @RequestParam(required = false) Long roomId) {

        if (roomId != null) {
            return searchRoomById(roomId);
        } else if (roomName != null) {
            return searchRoomByName(roomName);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(InvalidRequestMessages.EMPTY_SEARCH_CONDITION.getMessage());
    }

    public ResponseEntity<?> searchRoomById(Long roomId) {

        Room room = roomRepository.findById(roomId).orElse(null);

        if (room == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(NotFoundMessages.ROOM_ID.getMessage());
        }

        RoomResponseDto response = new RoomResponseDto(room);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> searchRoomByName(String roomName) {

        List<Room> rooms = roomRepository.findRoomNameContaining(roomName);

        if (rooms.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(NotFoundMessages.ROOM_NAME.getMessage());
        }

        List<RoomResponseDto> responseList =
                rooms.stream().map(RoomResponseDto::new).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }
}
