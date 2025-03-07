package com.example.web2_3_ourtuft_be.room.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RoomResponseDto {
    private final Long roomId;
    private final String roomName;
    private final int round;
    private final Long hostId;
    private final boolean disclosure;
    private final QuizSetType gameType;
    private final int time;
    private final int maxUsers;

    public RoomResponseDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.round = room.getRound();
        this.hostId = room.getHostId();
        this.disclosure = room.isDisclosure();
        this.gameType = room.getGameType();
        this.time = room.getTime();
        this.maxUsers = room.getMaxUsers();
    }

    public record GetPlayerInRoom(String userId, String username, String isReady) {
        public static GetPlayerInRoom of(String userId, String username, String isReady) {
            return new GetPlayerInRoom(userId, username, isReady);
        }
    }

    public record GetPlayersInRoom(Long hosId, List<GetPlayerInRoom> dto) {
        public static GetPlayersInRoom of(Long hostId, List<GetPlayerInRoom> dto) {
            return new GetPlayersInRoom(hostId, dto);
        }
    }
}
