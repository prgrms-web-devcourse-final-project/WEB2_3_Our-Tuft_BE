package com.example.web2_3_ourtuft_be.lobby.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.GlobalException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final RoomRepository roomRepository;

    public List<RoomResponseDto> searchRoom(String roomName, Long roomId) {

        List<Room> rooms = new ArrayList<>();

        if (roomId != null) {

            Room room = roomRepository.findById(roomId).orElse(null);
            if (room == null) {
                throw new GlobalException(
                        NotFoundMessages.ROOM_ID.getMessage(), HttpStatus.NOT_FOUND);
            }
            rooms.add(room);

        } else if (roomName != null) {

            rooms = roomRepository.findRoomNameContaining(roomName);

            if (rooms.isEmpty()) {
                throw new GlobalException(
                        NotFoundMessages.ROOM_NAME.getMessage(), HttpStatus.NOT_FOUND);
            }

        } else {
            throw new GlobalException(
                    InvalidRequestMessages.EMPTY_SEARCH_CONDITION.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }

        return rooms.stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }
}
