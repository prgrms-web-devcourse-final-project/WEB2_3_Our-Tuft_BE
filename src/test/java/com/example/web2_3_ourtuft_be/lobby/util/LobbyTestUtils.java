package com.example.web2_3_ourtuft_be.lobby.util;

import com.example.web2_3_ourtuft_be.room.entity.Room;

public class LobbyTestUtils {

    public static Room createRoom(
            String roomName,
            int peopleEntering,
            int round,
            String gameStatus,
            String host,
            boolean disclosure) {
        return Room.builder()
                .roomName(roomName)
                //                .peopleEntering(peopleEntering)
                .round(round)
                //                .gameStatus(gameStatus)
                //                .host(host)
                .disclosure(disclosure)
                .build();
    }
}
