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
public class RoomSocketService {

    private final LobbyService lobbyService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ParticipantService participantService;
    private final WebSocketService webSocketService;


    public void enterRoom(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        String userId = webSocketService.getUserIdFromSession(headerAccessor);


        // room 구독자를 관리하는 레디스 키에 입장한 사용자를 추가한다.
        participantService.addParticipantToRoom(roomId, userId, username);

        // 방설정 값을 가져온다.




        // room 구독자들에게 입장 알림 전송
        simpMessagingTemplate.convertAndSend(
                "/topic/room/"+roomId,
                WebSocketResponse.Send.of("SYSTEM", username + "님이 입장하였습니다"));

    }



}
