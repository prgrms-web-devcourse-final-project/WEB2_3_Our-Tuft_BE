package com.example.web2_3_ourtuft_be.room.dto;

import com.example.web2_3_ourtuft_be.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponseDto {
    private final Long roomId;
    private final String roomName;
    private final int peopleEntering;
    private final int round;
    private final String gameStatus;

    public RoomResponseDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.peopleEntering = room.getPeopleEntering();
        this.round = room.getRound();
        this.gameStatus = room.getGameStatus();
    }
}
