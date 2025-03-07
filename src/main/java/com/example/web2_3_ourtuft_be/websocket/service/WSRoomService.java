package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSRoomService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final ParticipantService participantService;
    private final WebSocketService webSocketService;
    private final LobbyService lobbyService;

    public void handleRoomEvent(
            SimpMessageHeaderAccessor headerAccessor, String roomId, String event) {
        if ("PLAYER_CHANGE_READY".equals(event)) changeReadyStatus(headerAccessor, roomId);
    }

    private void changeReadyStatus(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String key = participantService.getReadyStatusKey(roomId);
        String playerId = webSocketService.getUserIdFromSession(headerAccessor);

        String currentStatus = (String) redisTemplate.opsForHash().get(key, playerId);

        if ("false".equals(currentStatus)) currentStatus = "true";
        else currentStatus = "false";

        redisTemplate.opsForHash().put(key, playerId, currentStatus);

        sendSystemMessage(roomId, "PLAYER_CHANGE_READY");
    }

    public void addPlayer(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String userId = webSocketService.getUserIdFromSession(headerAccessor);
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        addPlayerToRoom(roomId, userId, username);

        sendEvent(roomId, "PLAYER_ADDED");
        sendSystemMessage(roomId, username + "님이 입장하였습니다");
    }

    private void addPlayerToRoom(String roomId, String userId, String username) {

        String participantOrderKey = participantService.getParticipantsOrderKey(roomId);
        String participantInfoKey = participantService.getParticipantsInfoKey(roomId);

        redisTemplate
                .opsForZSet()
                .add(participantOrderKey, userId, participantService.getTimeStamp());
        redisTemplate.opsForHash().put(participantInfoKey, userId, username);

        if (!"lobby".equals(roomId)) {
            String readyStatusKey = participantService.getReadyStatusKey(roomId);
            redisTemplate.opsForHash().put(readyStatusKey, userId, "false");
        }
    }

    public void removePlayerFromRoom(String roomId, String userId) {
        String participantOrderKey = participantService.getParticipantsOrderKey(roomId);
        String participantInfoKey = participantService.getParticipantsInfoKey(roomId);

        redisTemplate.opsForZSet().remove(participantOrderKey, userId);
        redisTemplate.opsForHash().delete(participantInfoKey, userId);

        if (!"lobby".equals(roomId)) {
            String readyStatusKey = participantService.getReadyStatusKey(roomId);
            redisTemplate.opsForHash().delete(readyStatusKey, userId);
        }
    }

    public void removePlayer(String roomId, String userId, String username) {

        boolean isLobby = "lobby".equals(roomId);
        boolean isHost = lobbyService.isHost(Long.valueOf(roomId), Long.valueOf(userId));

        removePlayerFromRoom(roomId, userId);

        int remaining = participantService.getPlayersInRoom(roomId).size();

        if (remaining == 0) {
            lobbyService.deleteRoom(Long.valueOf(roomId));
            return;
        }

        if (!isLobby && isHost) {
            String newHostId = participantService.getNextHost(roomId);
            if (newHostId != null) {
                lobbyService.changeRoomHost(Long.valueOf(roomId), Long.parseLong(newHostId));
                sendEvent(roomId, "HOST_CHANGED");
                sendSystemMessage(roomId, "방장이 변경되었습니다.");
            }
        }

        if (!isLobby && !isHost) {
            sendEvent(roomId, "PLAYER_DISCONNECTED");
            sendSystemMessage(roomId, username + "님이 퇴장하였습니다");
        }
    }

    public void sendEvent(String roomId, String event) {
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId, WebSocketResponse.SendEvent.of(event));
    }

    public void sendSystemMessage(String roomId, String message) {
        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId, WebSocketResponse.Send.of("SYSTEM", message));
    }
}
