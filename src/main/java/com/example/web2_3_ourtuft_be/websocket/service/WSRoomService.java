package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.redis.service.RoomSettingService;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.websocket.event.EVENT;
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
    private final RoomSettingService roomSettingService;

    public void handleRoomEvent(
            SimpMessageHeaderAccessor headerAccessor, String roomId, String event) {

        if (event.contains(EVENT.READY.getValue())) changeReadyStatus(headerAccessor, roomId);
        if (event.contains(EVENT.SWITCHING_ROOM_TO_GAME.getValue())) {}
        if (event.contains(EVENT.READY.getValue()))
            changeReadyStatus(headerAccessor, roomId);
        if (event.contains(EVENT.SWITCHING_ROOM_TO_GAME.getValue())) {
            if (roomQuizService.checkQuizIds(roomId)) {
                savePlayerCount(roomId);
                webSocketService.changeSessionFlag(headerAccessor);
                lobbyService.changeRoomPlayingStatus(roomId);
            }
        }
        if (roomQuizService.checkQuizIds(roomId)) {
            savePlayerCount(roomId);
            webSocketService.changeSessionFlag(headerAccessor);
            lobbyService.changeRoomPlayingStatus(roomId);
        }
        if (event.contains(EVENT.GAME_STARTED.getValue()))
            wsGameService.startGame(headerAccessor, roomId);
        if (event.contains(EVENT.GAME_END.getValue())) {
            String winnerId = wsGameService.getWinnerId(roomId);
            wsGameService.endGame(headerAccessor, roomId, winnerId);
        }
    }

    private void savePlayerCount(String roomId) {
        String key = participantService.getParticipantsInfoKey(roomId);
        int count = redisTemplate.opsForHash().keys(key).size();

        String totalCntKey = participantService.getPlayerTotalCountKey(roomId);
        redisTemplate.opsForValue().set(totalCntKey, String.valueOf(count));

        redisTemplate
                .opsForValue()
                .set(participantService.getPlayerCurrentCountKey(roomId), String.valueOf(0));

        webSocketService.sendEvent(roomId, "SWITCHING_ROOM_TO_GAME");
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

            Boolean exists = redisTemplate.opsForHash().hasKey(readyStatusKey, userId);
            if (!exists) {
                redisTemplate.opsForHash().put(readyStatusKey, userId, "false");
            }
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
        boolean isHost = false;

        if (!isLobby) {
            isHost = lobbyService.isHost(Long.valueOf(roomId), Long.valueOf(userId));
        }

        int remaining = participantService.getPlayersInRoom(roomId).size();

        removePlayerFromRoom(roomId, userId);

        if (!isLobby) {
            if (remaining == 1) {
                lobbyService.deleteRoom(Long.valueOf(roomId));
                return;
            }

            if (isHost) {
                String newHostId = participantService.getNextHost(roomId);
                if (newHostId != null) {
                    lobbyService.changeRoomHost(Long.valueOf(roomId), Long.parseLong(newHostId));
                    webSocketService.sendEvent(roomId, "HOST_CHANGED");
                    webSocketService.sendSystemMessage(roomId, "방장이 변경되었습니다.");
                }
            }

            webSocketService.sendEvent(roomId, "PLAYER_DISCONNECTED");
            webSocketService.sendSystemMessage(roomId, username + "님이 퇴장하였습니다");
        }
    }
}
