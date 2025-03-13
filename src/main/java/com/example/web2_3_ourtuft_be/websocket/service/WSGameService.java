package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.redis.service.RoomSettingService;
import com.example.web2_3_ourtuft_be.redis.service.RoomStatusService;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSGameService {

    private final RoomQuizService roomQuizService;
    private final WebSocketService webSocketService;
    private final RedisTemplate<String, String> redisTemplate;
    private final ParticipantService participantService;
    private final RoomStatusService roomStatusService;
    private final RoomSettingService roomSettingService;
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    private final Map<String, ScheduledFuture<?>> gameSchedulers = new HashMap<>();
    private final LobbyService lobbyService;
    private final UserService userService;

    public void gameSetting(String roomId, SimpMessageHeaderAccessor headerAccessor) {
        roomStatusService.setGameStatus(Long.valueOf(roomId), "RUNNING");
        roomStatusService.setCurrentRound(Long.valueOf(roomId), 0);
        initializePlayerScores(roomId);
        setPlayerCorrectFlag(roomId);
    }

    public void startGame(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        int totalRound = Integer.parseInt(getTotalRound(roomId));
        int timeLimit = Integer.parseInt(getTimeLimit(roomId));
        webSocketService.changeSessionFlag(headerAccessor);

        roomQuizService.setQuiz(roomId, totalRound);

        // FixedDelay
        scheduledQuizWithFixedDelay(headerAccessor, roomId, totalRound, timeLimit);
    }

    private void scheduledQuizWithFixedDelay(
            SimpMessageHeaderAccessor headerAccessor,
            String roomId,
            int totalRound,
            int timeLimit) {
        taskScheduler.initialize();

        ScheduledFuture<?> scheduledFuture =
                taskScheduler.scheduleWithFixedDelay(
                        () -> sendQuiz(headerAccessor, roomId, totalRound),
                        timeLimit * 1000L); // timeLimit 초 이후 실행

        gameSchedulers.put(roomId, scheduledFuture);
    }

    public void submitAnswer(
            String roomId, SimpMessageHeaderAccessor headerAccessor, String answer) {
        String userId = webSocketService.getUserIdFromSession(headerAccessor);
        String username = webSocketService.getUsernameFromSession(headerAccessor);

        String correctFlag = getPlayerCorrectFlag(roomId, userId);

        if ("true".equals(correctFlag)) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        int currentRound = roomStatusService.getCurrentRound(Long.valueOf(roomId));
        if (currentRound <= 0) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        String quizId = roomQuizService.getQuizIdByRoom(roomId, currentRound - 1);
        if (quizId == null || quizId.trim().isEmpty()) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        Long quizIdLong;
        try {
            quizIdLong = Long.valueOf(quizId);
        } catch (NumberFormatException e) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        String quizKey = roomQuizService.getQuizKey(quizIdLong);
        String correctAnswer = roomQuizService.getQuizDetail("answer", quizKey);

        if (correctAnswer == null) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        if (answer.trim().equalsIgnoreCase(correctAnswer.trim())) {
            changePlayerCorrectFlag(roomId, userId);
            incrementPlayerScore(roomId, userId);
            webSocketService.sendGameSystemMessageToUser(userId, roomId, "정답입니다!");
            webSocketService.sendGameSystemMessage(roomId, username + "님이 정답을 맞추셨습니다!");
        } else webSocketService.sendGameMessage(roomId, username, answer);
    }

    public void submitOXAnswer(
            String roomId, SimpMessageHeaderAccessor headerAccessor, String answer) {

        String userId = webSocketService.getUserIdFromSession(headerAccessor);
        String username = webSocketService.getUsernameFromSession(headerAccessor);
        String correctFlag = getPlayerCorrectFlag(roomId, userId);

        if ("true".equals(correctFlag)) return;

        int currentRound = roomStatusService.getCurrentRound(Long.valueOf(roomId));
        if (currentRound <= 0) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        String quizId = roomQuizService.getQuizIdByRoom(roomId, currentRound - 1);
        if (quizId == null || quizId.trim().isEmpty()) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        Long quizIdLong;
        try {
            quizIdLong = Long.valueOf(quizId);
        } catch (NumberFormatException e) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        String quizKey = roomQuizService.getQuizKey(quizIdLong);
        String correctAnswer = roomQuizService.getQuizDetail("answer", quizKey);

        if (correctAnswer == null) {
            webSocketService.sendGameMessage(roomId, username, answer);
            return;
        }

        if (answer.trim().equalsIgnoreCase(correctAnswer.trim())) {
            changePlayerCorrectFlag(roomId, userId);
            incrementPlayerScore(roomId, userId);
            webSocketService.sendGameEvent(roomId, "PLAYER_" + userId + "_CORRECTED");
        } else {
            webSocketService.sendGameEvent(roomId, "PLAYER_" + userId + "_UNCORRECTED");
            changePlayerCorrectFlag(roomId, userId);
        }
    }

    public void sendQuiz(SimpMessageHeaderAccessor headerAccessor, String roomId, int totalRound) {
        //        String userId = webSocketService.getUserIdFromSession(headerAccessor);
        setPlayerCorrectFlag(roomId);

        int currentRound = roomStatusService.getCurrentRound(Long.valueOf(roomId));
        currentRound += 1;
        roomStatusService.setCurrentRound(Long.valueOf(roomId), currentRound);
        if (currentRound > totalRound) {
            webSocketService.sendGameSystemMessage(roomId, "게임이 종료되었습니다.");
            endGame(headerAccessor, roomId, getWinnerId(roomId));
            return;
        }

        String quizId = roomQuizService.getQuizIdByRoom(roomId, currentRound - 1);
        webSocketService.sendGameSystemMessage(roomId, currentRound + " 라운드가 시작됩니다.");

        if (quizId == null || quizId.trim().isEmpty()) {
            webSocketService.sendGameSystemMessage(roomId, "모든 퀴즈가 소진되었습니다.");
            webSocketService.sendGameSystemMessage(roomId, "게임이 종료되었습니다.");
            //            endGame(headerAccessor, roomId, getWinnerId(roomId));
            return;
        }

        Long quizIdLong;
        try {
            quizIdLong = Long.valueOf(quizId);
        } catch (NumberFormatException e) {
            System.err.println("퀴즈 ID 변환 실패: " + quizId);
            webSocketService.sendGameSystemMessage(roomId, "퀴즈 ID 형식 오류가 발생했습니다.");
            webSocketService.sendGameSystemMessage(roomId, "게임을 종료합니다.");
            endGame(headerAccessor, roomId, getWinnerId(roomId));
            return;
        }

        String quizKey = roomQuizService.getQuizKey(quizIdLong);
        String question = roomQuizService.getQuizDetail("question", quizKey);

        if (question == null) {
            System.err.println("퀴즈 상세 정보를 불러오지 못했습니다. quizKey: " + quizKey);
            webSocketService.sendGameSystemMessage(roomId, "퀴즈 상세 정보 오류");
            webSocketService.sendGameSystemMessage(roomId, "게임을 종료합니다.");
            endGame(headerAccessor, roomId, getWinnerId(roomId));
            return;
        }

        webSocketService.sendGameQuizMessage(roomId, "question", question);
    }

    private void setPlayerCorrectFlag(String roomId) {
        String key = getPlayerCorrectFlagKey(roomId);
        String playersKey = getPlayerInfoKey(roomId);

        Set<Object> hashKeys = redisTemplate.opsForHash().keys(playersKey);

        for (Object hashKey : hashKeys) {
            redisTemplate.opsForHash().put(key, hashKey, "false");
        }
    }

    public void endGame(SimpMessageHeaderAccessor headerAccessor, String roomId, String winnerId) {
        endSchedule(roomId);
        createNewRoom(roomId, winnerId);
        deleteGameInfo(roomId);
    }

    public void endSchedule(String roomId) {
        ScheduledFuture<?> scheduledFuture = gameSchedulers.get(roomId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            gameSchedulers.remove(roomId);
        }
    }

    public void createNewRoom(String roomId, String winnerId) {
        RoomResponseDto dto =
                lobbyService.createRoom(getCreateRoomDTO(roomId), Long.valueOf(winnerId));
        webSocketService.sendGameEvent(roomId, "NEW_ROOM_CREATED_" + dto.getRoomId());
    }

    public String getWinnerId(String roomId) {
        String playerScoreKey = getPlayerScoreKey(roomId);

        Set<String> topScorerSet = redisTemplate.opsForZSet().reverseRange(playerScoreKey, 0, 0);
        if (topScorerSet == null || topScorerSet.isEmpty()) {
            return "";
        }

        return topScorerSet.iterator().next();
    }

    private RoomRequestDto getCreateRoomDTO(String roomId) {
        return roomSettingService.getRoomSettingsFromRedis(roomId);
    }

    private String getPlayerCorrectFlag(String roomId, String userId) {
        String key = getPlayerCorrectFlagKey(roomId);

        String flag = redisTemplate.opsForHash().get(key, userId).toString();

        return flag != null ? flag : "false";
    }

    private void deleteGameInfo(String roomId) {
        deletePlayerCount(roomId);
        deleteGame(roomId);
        deleteQuizInfo(roomId);
        roomSettingService.deleteRoomSettingToRedis(roomId);
        roomStatusService.deleteReadyStatus(Long.valueOf(roomId));
        roomStatusService.deleteRound(Long.valueOf(roomId));
    }

    private void deleteQuizInfo(String roomId) {
        redisTemplate.delete(roomQuizService.getQuizOrderKey(roomId));
        redisTemplate.delete(roomQuizService.getRoomQuizSetKey(Long.valueOf(roomId)));
    }

    private void deletePlayerCount(String roomId) {
        redisTemplate.delete(participantService.getPlayerTotalCountKey(roomId));
        redisTemplate.delete(participantService.getPlayerCurrentCountKey(roomId));
    }

    private void deleteGame(String roomId) {
//        redisTemplate.delete(getPlayerInfoKey(roomId));

        redisTemplate.delete(getPlayerOrderKey(roomId));
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

        User user = userService.getUser(Long.parseLong(userId));

        redisTemplate
                .opsForZSet()
                .add(participantOrderKey, userId, participantService.getTimeStamp());
        redisTemplate.opsForHash().put(participantInfoKey, userId, username);

        setPlayerCurrentCount(roomId);
    }

    private void changePlayerCorrectFlag(String roomId, String userId) {
        String key = getPlayerCorrectFlagKey(roomId);

        if ("true".equals(redisTemplate.opsForHash().get(key, userId))
                || redisTemplate.opsForHash().get(key, userId) == null) {
            redisTemplate.opsForHash().put(key, userId, "false");
        } else {
            redisTemplate.opsForHash().put(key, userId, "true");
        }
    }

    public void initializePlayerScores(String roomId) {
        String playerScoreKey = getPlayerScoreKey(roomId);
        String playerOrderKey = getPlayerOrderKey(roomId);

        Set<String> range = redisTemplate.opsForZSet().range(playerOrderKey, 0, -1);

        if (range != null) {
            for (String player : range) {
                redisTemplate.opsForZSet().add(playerScoreKey, player, 0);
            }
        }
    }

    public void incrementPlayerScore(String roomId, String playerId) {
        String playerScoreKey = getPlayerScoreKey(roomId);
        redisTemplate.opsForZSet().incrementScore(playerScoreKey, playerId, 1);
    }

    private String getPlayerOrderKey(String roomId) {
        return "game:participants:order:" + roomId;
    }

    public String getPlayerInfoKey(String roomId) {
        return "game:participants:info:" + roomId;
    }

    public String getPlayerScoreKey(String roomId) {
        return "game:participants:score:" + roomId;
    }

    public String getPlayerCorrectFlagKey(String roomId) {
        return "game:participants:correct:" + roomId;
    }
}
