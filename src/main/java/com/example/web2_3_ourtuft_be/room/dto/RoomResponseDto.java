package com.example.web2_3_ourtuft_be.room.dto;

import com.example.web2_3_ourtuft_be.room.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponseDto {
    private final Long roomId;
    private final String roomName;
    private final int round;
    private final String host;
    private final boolean disclosure;

    public RoomResponseDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.round = room.getRound();
        this.host = room.getHost();
        this.disclosure = room.isDisclosure();
    }
}
