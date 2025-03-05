package com.example.web2_3_ourtuft_be.websocket.dto;

import com.example.web2_3_ourtuft_be.redis.dto.RoomSettingsDto;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomSubscribeResponse {
    private RoomSettingsDto roomSettings; // 방 설정 정보
    private Map<String, String> participants; // 대기실 참가자 목록

    @Builder
    public RoomSubscribeResponse(RoomSettingsDto roomSettings, Map<String, String> participants) {
        this.roomSettings = roomSettings;
        this.participants = participants;
    }

    public static RoomSubscribeResponse of(
            RoomSettingsDto roomSettings, Map<String, String> participants) {
        return RoomSubscribeResponse.builder()
                .roomSettings(roomSettings)
                .participants(participants)
                .build();
    }
}
