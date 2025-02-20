package com.example.web2_3_ourtuft_be.lobby.service;

import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LobbyService {

    private final RoomRepository roomRepository;

    public List<RoomResponseDto> searchRoomByName(String roomName) {
        List<Room> rooms;

        if(roomName != null && !roomName.isEmpty()) {
            rooms = roomRepository.findRoomNameContaining(roomName);
        } else {
            rooms = roomRepository.findAll();
        }

        return rooms.stream()
                .map(room -> new RoomResponseDto(
                        room.getId(),
                        room.getRoomName(),
                        room.getPeopleEntering(),
                        room.getRound(),
                        room.getGameStatus()
                ))
                .collect(Collectors.toList());
    }
}
