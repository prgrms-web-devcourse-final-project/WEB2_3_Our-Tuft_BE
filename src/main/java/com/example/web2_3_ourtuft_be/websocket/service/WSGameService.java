package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.redis.service.RoomSettingService;
import com.example.web2_3_ourtuft_be.redis.service.RoomStatusService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSGameService {

    private final RoomQuizService roomQuizService;

    private final WebSocketService webSocketService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ParticipantService participantService;
    private final RoomStatusService roomStatusService;
    private final RoomSettingService roomSettingService;

    public void gameSetting(String roomId, SimpMessageHeaderAccessor headerAccessor) {
        roomStatusService.setGameStatus(Long.valueOf(roomId), "RUNNING");
        roomStatusService.setCurrentRound(Long.valueOf(roomId), 0);
    }

    public void startGame(String roomId) {
        int totalRound = Integer.parseInt(getTotalRound(roomId));
        int timeLimit = Integer.parseInt(getTimeLimit(roomId));
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
        scheduledExecutorService.scheduleAtFixedRate(
                () -> sendQuiz(roomId, totalRound), 0, timeLimit, TimeUnit.SECONDS);
    }

    public void sendQuiz(String roomId, int totalRound) {
        int currentRound = roomStatusService.getCurrentRound(Long.valueOf(roomId));
        webSocketService.sendGameSystemMessage(roomId, "테스트");
    }

    private String getTotalRound(String roomId) {
        return (String)
                redisTemplate
                        .opsForHash()
                        .get(roomSettingService.getRoomQuizSetKey(roomId), "rounds");
    }

    private String getTimeLimit(String roomId) {
        return (String)
                redisTemplate
                        .opsForHash()
                        .get(roomSettingService.getRoomQuizSetKey(roomId), "timelimit");
    }

    public void addPlayer(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String userId = webSocketService.getUserIdFromSession(headerAccessor);
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        addPlayerToGame(roomId, userId, username);

        webSocketService.sendGameEvent(roomId, "PLAYER_ADDED");
        webSocketService.sendGameSystemMessage(roomId, username + "님이 입장하였습니다");

        validPlayerCount(roomId);
    }

    private void setPlayerCurrentCount(String roomId) {
        String currentCntKey = participantService.getPlayerCurrentCountKey(roomId);
        String currentCount = String.valueOf(redisTemplate.opsForValue().get(currentCntKey));

        if (currentCount == null) {
            currentCount = "0";
        }

        int count = 0;

        count = Integer.parseInt(currentCount);

        count += 1;

        redisTemplate.opsForValue().set(currentCntKey, String.valueOf(count));
    }

    private void validPlayerCount(String roomId) {
        String totalCntKey = participantService.getPlayerTotalCountKey(roomId);
        String currentCntKey = participantService.getPlayerCurrentCountKey(roomId);

        String totalCnt = String.valueOf(redisTemplate.opsForValue().get(totalCntKey));
        String currentCnt = String.valueOf(redisTemplate.opsForValue().get(currentCntKey));

        if (totalCnt.equals(currentCnt)) {
            webSocketService.sendGameEvent(roomId, "ALL_CONNECTED");
            redisTemplate.delete(totalCntKey);
            redisTemplate.delete(currentCntKey);
        }
    }

    private void addPlayerToGame(String roomId, String userId, String username) {
        String participantOrderKey = getPlayerOrderKey(roomId);
        String participantInfoKey = getPlayerInfoKey(roomId);

        redisTemplate
                .opsForZSet()
                .add(participantOrderKey, userId, participantService.getTimeStamp());
        redisTemplate.opsForHash().put(participantInfoKey, userId, username);

        setPlayerCurrentCount(roomId);
    }

    private String getPlayerOrderKey(String roomId) {
        return "game:participants:order:" + roomId;
    }

    public String getPlayerInfoKey(String roomId) {
        return "game:participants:info:" + roomId;
    }

    private void validateQuizRedis(String roomId) {}

    //    // 게임방에서 보내는 채팅
    //    // 정답 체크 필요
    //    public void processGameRoom(
    //            SimpMessageHeaderAccessor headerAccessor, Long roomId, String message) {
    //
    //        String userId = getUserIdFromSession(headerAccessor);
    //        String username = getUsernameFromSession(headerAccessor);
    //        String gameStatus = roomStatusService.getGameStatus(roomId);
    //
    //        // 게임이 진행 중 일때
    //        if (gameStatus.equals(GameStatus.RUNNING.name())) {
    //            String correctAnswer = getCorrectAnswerFromRedis(roomId);
    //
    //            if (correctAnswer.equalsIgnoreCase(message.trim())) { // 정답 맞췄을 때 - 대소문자 구분없이 비교
    //                increaseUserScore(roomId, userId);
    //
    //                sendSystemMessageToUser(userId, roomId, "정답입니다!");
    //                sendSystemMessage(String.valueOf(roomId), username + "님이 정답을 맞췄습니다!");
    //
    //                return;
    //            }
    //        }
    //        messagingTemplate.convertAndSend(
    //                "/topic/gameRoom/" + roomId, WebSocketResponse.Send.of(username, message));
    //    }
    //
    //    // TODO: 현재 라운드 정답 가져오는 함수
    //    public String getCorrectAnswerFromRedis(Long roomId) {
    //
    //        return "함수 채워야함";
    //    }
    //
    //    public void increaseUserScore(Long roomId, String userId) {
    //    }
}
