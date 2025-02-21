package com.example.web2_3_ourtuft_be.lobby.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final RoomRepository roomRepository;

    public List<RoomResponseDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();

        if(rooms.isEmpty()) {
            throw new NotFoundException(NotFoundMessages.ROOM);
        }

        return rooms.stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }

    public List<RoomResponseDto> searchRoom(String roomName, Long roomId) {

        List<Room> rooms = new ArrayList<>();

        if (roomId != null) {

            Room room = roomRepository.findById(roomId).orElse(null);
            if (room == null) {
                throw new NotFoundException(NotFoundMessages.ROOM_ID);
            }
            rooms.add(room);

        } else if (roomName != null) {

            rooms = roomRepository.findRoomNameContaining(roomName);

            if (rooms.isEmpty()) {
                throw new NotFoundException(NotFoundMessages.ROOM_NAME);
            }

        } else {
            throw new InvalidRequestException(InvalidRequestMessages.EMPTY_SEARCH_CONDITION);
        }

        return rooms.stream().map(RoomResponseDto::new).collect(Collectors.toList());
    }
}
