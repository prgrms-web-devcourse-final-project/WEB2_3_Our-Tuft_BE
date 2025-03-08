package com.example.web2_3_ourtuft_be.room.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final LobbyService lobbyService;

    public QuizSetType getGameTypeByRoomId(Long roomId) {
        return roomRepository
                .findById(roomId)
                .map(Room::getGameType)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.ROOM_ID));
    }

    public int getRoundByRoomId(Long roomId) {
        Room room = lobbyService.findByRoomId(roomId);

        return room.getRound();
    }

    public Long getHostIdByRoomId(String roomId) {
        if ("lobby".equals(roomId))
            throw new InvalidRequestException(InvalidRequestMessages.INVALID_ROOM_ID);

        Room room = lobbyService.findByRoomId(Long.valueOf(roomId));

        return room.getHostId();
    }
}
