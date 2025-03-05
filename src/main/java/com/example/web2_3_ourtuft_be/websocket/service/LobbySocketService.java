package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.websocket.dto.LobbySubscribeResponse;
import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LobbySocketService {

    private final LobbyService lobbyService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ParticipantService participantService;
    private final WebSocketService webSocketService;

    public LobbySubscribeResponse enterLobby(SimpMessageHeaderAccessor headerAccessor) {
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        String userId = webSocketService.getUserIdFromSession(headerAccessor);

        // Lobby 구독자를 관리하는 레디스 키에 입장한 사용자를 추가한다.
        participantService.addParticipantToLobby(userId, username);

        // 구독하고있는 사용자들을 가져온다
        Map<String, String> participants = participantService.getParticipants("lobby");
        // 개설된 방 목록을 가져온다
        List<RoomResponseDto> roomList = lobbyService.getAllRooms();

        LobbySubscribeResponse lobbySubscribeResponse = LobbySubscribeResponse.of(participants,roomList );

        // lobby 구독자들 화면에 새로운 입장자를 포함한 목록 전달
        simpMessagingTemplate.convertAndSend("/topic/room/lobby",participants );
        // Lobby 구독자들에게 입장 알림 전송
        simpMessagingTemplate.convertAndSend(
                "/topic/room/lobby",
                WebSocketResponse.Send.of("SYSTEM", username + "님이 입장하였습니다"));

        return lobbySubscribeResponse;

    }
}
