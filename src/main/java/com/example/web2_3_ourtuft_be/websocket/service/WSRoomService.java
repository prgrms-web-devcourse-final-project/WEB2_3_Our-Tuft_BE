package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSRoomService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ParticipantService participantService;
    private final WebSocketService webSocketService;
    private final LobbyService lobbyService;
    private final WSGameService wsGameService;
    private final RoomQuizService roomQuizService;

    public void handleRoomEvent(
            SimpMessageHeaderAccessor headerAccessor, String roomId, String event) {

        if (event.contains("PLAYER_CHANGE_READY")) changeReadyStatus(headerAccessor, roomId);
        if (event.contains("SWITCHING_ROOM_TO_GAME"))
            if (roomQuizService.checkQuizIds(roomId)) savePlayerCount(roomId);
        if (event.contains("GAME_STARTED")) wsGameService.startGame(roomId);
    }

    private void existsQuizSet(String roomId) {}

    //    private void changeDisconnectionFlag(SimpMessageHeaderAccessor headerAccessor) {
    //        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("isSwitchingGame",
    // true);
    //    }

    private void savePlayerCount(String roomId) {
        String key = participantService.getParticipantsInfoKey(roomId);
        int count = redisTemplate.opsForHash().keys(key).size();

        String totalCntKey = participantService.getPlayerTotalCountKey(roomId);
        redisTemplate.opsForValue().set(totalCntKey, String.valueOf(count));

        redisTemplate
                .opsForValue()
                .set(participantService.getPlayerCurrentCountKey(roomId), String.valueOf(0));
    }

    private void changeReadyStatus(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String key = participantService.getReadyStatusKey(roomId);
        String playerId = webSocketService.getUserIdFromSession(headerAccessor);

        String currentStatus = (String) redisTemplate.opsForHash().get(key, playerId);

        if ("false".equals(currentStatus)) currentStatus = "true";
        else currentStatus = "false";

        redisTemplate.opsForHash().put(key, playerId, currentStatus);

        webSocketService.sendEvent(roomId, "PLAYER_CHANGE_READY");
    }

    public void addPlayer(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String userId = webSocketService.getUserIdFromSession(headerAccessor);
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        addPlayerToRoom(roomId, userId, username);

        webSocketService.sendEvent(roomId, "PLAYER_ADDED");
        webSocketService.sendSystemMessage(roomId, username + "님이 입장하였습니다");
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
        int remaining = participantService.getPlayersInRoom(roomId).size();

        removePlayerFromRoom(roomId, userId);

        if (remaining == 1) {
            lobbyService.deleteRoom(Long.valueOf(roomId));
            return;
        }

        if (!isLobby && isHost) {
            String newHostId = participantService.getNextHost(roomId);
            if (newHostId != null) {
                lobbyService.changeRoomHost(Long.valueOf(roomId), Long.parseLong(newHostId));
                webSocketService.sendEvent(roomId, "HOST_CHANGED");
                webSocketService.sendSystemMessage(roomId, "방장이 변경되었습니다.");
            }
        }

        if (!isLobby) {
            webSocketService.sendEvent(roomId, "PLAYER_DISCONNECTED");
            webSocketService.sendSystemMessage(roomId, username + "님이 퇴장하였습니다");
        }
    }
}
