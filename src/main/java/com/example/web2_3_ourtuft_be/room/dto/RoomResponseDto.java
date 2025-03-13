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
    private final boolean isGameRunning;
    private final int currentPlayer;

    public RoomResponseDto(Room room, int currentPlayer) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.round = room.getRound();
        this.hostId = room.getHostId();
        this.disclosure = room.isDisclosure();
        this.gameType = room.getGameType();
        this.time = room.getTime();
        this.maxUsers = room.getMaxUsers();
        this.isGameRunning = room.isGameRunning();
        this.currentPlayer = currentPlayer;
    }

    public RoomResponseDto(Room room) {
        this.roomId = room.getId();
        this.roomName = room.getRoomName();
        this.round = room.getRound();
        this.hostId = room.getHostId();
        this.disclosure = room.isDisclosure();
        this.gameType = room.getGameType();
        this.time = room.getTime();
        this.maxUsers = room.getMaxUsers();
        this.isGameRunning = room.isGameRunning();
        currentPlayer = 0;
    }

    public record GetPlayerInRoom(
            String userId,
            String username,
            String isReady,
            String eye,
            String mouth,
            String skin,
            String nickColor) {
        public static GetPlayerInRoom of(
                String userId,
                String username,
                String isReady,
                String eye,
                String mouth,
                String skin,
                String nickColor) {
            return new GetPlayerInRoom(userId, username, isReady, eye, mouth, skin, nickColor);
        }
    }

    public record GetPlayersInRoom(Long hostId, Integer currentPlayers, List<GetPlayerInRoom> dto) {
        public static GetPlayersInRoom of(
                Long hostId, Integer currentPlayers, List<GetPlayerInRoom> dto) {
            return new GetPlayersInRoom(hostId, currentPlayers, dto);
        }
    }

    public record GetPlayerInGame(
            String userId,
            String username,
            String eye,
            String mouth,
            String skin,
            String nickColor) {
        public static GetPlayerInGame of(
                String userId,
                String username,
                String eye,
                String mouth,
                String skin,
                String nickColor) {
            return new GetPlayerInGame(userId, username, eye, mouth, skin, nickColor);
        }
    }
}
