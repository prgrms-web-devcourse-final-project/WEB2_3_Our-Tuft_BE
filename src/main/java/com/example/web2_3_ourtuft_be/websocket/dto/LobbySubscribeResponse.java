package com.example.web2_3_ourtuft_be.websocket.dto;

import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LobbySubscribeResponse {
    private Map<String, String> participants;
    private List<RoomResponseDto> roomList;

    @Builder
    public LobbySubscribeResponse(
            Map<String, String> participants, List<RoomResponseDto> roomList) {
        this.participants = participants;
        this.roomList = roomList;
    }

    public static LobbySubscribeResponse of(
            Map<String, String> participants, List<RoomResponseDto> roomList) {
        return LobbySubscribeResponse.builder()
                .participants(participants)
                .roomList(roomList)
                .build();
    }
}
