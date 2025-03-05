package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.dto.RoomSettingsDto;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomSettingService;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.websocket.dto.RoomSubscribeResponse;
import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomSocketService {

    private final LobbyService lobbyService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ParticipantService participantService;
    private final WebSocketService webSocketService;
    private final RoomSettingService roomSettingService;

    public RoomSubscribeResponse enterRoom(
            SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        String userId = webSocketService.getUserIdFromSession(headerAccessor);

        // room 구독자를 관리하는 레디스 키에 입장한 사용자를 추가한다.
        participantService.addParticipantToRoom(roomId, userId, username);

        // 방설정 값을 가져온다.
        RoomSettingsDto roomSettings = roomSettingService.getRoomSettings(roomId);

        // 대기실 플레이어 목록을 가져온다.
        Map<String, String> participants = participantService.getParticipants(roomId);

        // room 구독자들에게 입장 알림 전송
        simpMessagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                WebSocketResponse.Send.of("SYSTEM", username + "님이 입장하였습니다"));

        RoomSubscribeResponse roomSubscribeResponse =
                RoomSubscribeResponse.of(roomSettings, participants);

        return roomSubscribeResponse;
    }
}
