package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.enums.GameStatus;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomStatusService;
import com.example.web2_3_ourtuft_be.websocket.dto.WebSocketResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final RoomStatusService roomStatusService;
    private final ParticipantService participantService;

    // 대기실에서 하는 채팅
    public void processRoomMessage(
            SimpMessageHeaderAccessor headerAccessor, Long roomId, String message) {
        String username = getUsernameFromSession(headerAccessor);

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId, WebSocketResponse.Send.of(username, message));
    }

    // 게임방에서 보내는 채팅
    // 정답 체크 필요
    public void processGameRoom(
            SimpMessageHeaderAccessor headerAccessor, Long roomId, String message) {

        String username = getUsernameFromSession(headerAccessor);
        String userId = getUserIdFromSession(headerAccessor);

        String gameStatus = roomStatusService.getGameStatus(roomId);

        // 게임이 진행 중 일때
        if (gameStatus.equals(GameStatus.RUNNING.name())) {
            String correctAnswer = getCorrectAnswerFromRedis(roomId);

            if (correctAnswer.equalsIgnoreCase(message.trim())) { // 정답 맞췄을 때 - 대소문자 구분없이 비교
                increaseUserScore(roomId, userId);

                // 정답자에게만 "정답입니다!" 전송
                messagingTemplate.convertAndSendToUser(
                        userId,
                        "/topic/gameRoom/" + roomId,
                        WebSocketResponse.Send.of("SYSTEM", "정답입니다!"));

                // 모든 게임방 인원에게 "@@님이 정답을 맞췄습니다!" 전송
                messagingTemplate.convertAndSend(
                        "/topic/gameRoom/" + roomId,
                        WebSocketResponse.Send.of("SYSTEM", username + "님이 정답을 맞췄습니다!"));

                return;
            }
        }
        messagingTemplate.convertAndSend(
                "/topic/gameRoom/" + roomId, WebSocketResponse.Send.of(username, message));
    }

    // TODO: 현재 라운드 정답 가져오는 함수
    public String getCorrectAnswerFromRedis(Long roomId) {

        return "함수 채워야함";
    }

    public void increaseUserScore(Long roomId, String userId) {}

    // 구독을 하면 방 정보에 유저가 추가
    // 로비는 세부 정보를 로비에 추가 X
    public void handleRoomSubscribe(SimpMessageHeaderAccessor headerAccessor, String roomId) {
        String username = getUsernameFromSession(headerAccessor);
        String userId = getUserIdFromSession(headerAccessor);

        if (roomId.equals("lobby")) {
            participantService.addParticipantToLobby(Long.parseLong(userId), username);
        } else {
            participantService.addParticipantToRoom(
                    Long.parseLong(roomId), Long.parseLong(userId), username);
        }

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId,
                WebSocketResponse.Send.of("SYSTEM", username + "님이 입장하였습니다"));
    }

    public WebSocketResponse.Send sendMessageToRoom(
            SimpMessageHeaderAccessor headerAccessor, String message) {
        String username = getUsernameFromSession(headerAccessor);
        return WebSocketResponse.Send.of(username, message);
    }

    // 핸드셰이크에서 Principal 객체에 회원 정보를 담으려고 했지만 Null 값이 넘어오는 문제를 아직 해결 X
    // 핸드셰이크 JWT 인증단계에서 attributes 에 키벨류로 담아뒀다
    public String getUserIdFromSession(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("userId");
    }

    // 핸드셰이크에서 Principal 객체에 회원 정보를 담으려고 했지만 Null 값이 넘어오는 문제를 아직 해결 X
    // 핸드셰이크 JWT 인증단계에서 attributes 에 키벨류로 담아뒀다
    public String getUsernameFromSession(SimpMessageHeaderAccessor headerAccessor) {
        return (String) headerAccessor.getSessionAttributes().get("username");
    }

    private void addParticipantToRoom(String roomId, String userId) {
        String key = "room:" + roomId + ":participants";

        // ZRANGE room:1:participants 0 -1 WITHSCORES, 로 조회
        redisTemplate.opsForZSet().add(key, userId, System.currentTimeMillis());
    }

    private void addParticipantDetailToRoom(String roomId, String userId, String username) {
        String key = "room:" + roomId + ":participant:" + userId;

        Map<String, String> participantDetail = new HashMap<>();

        participantDetail.put("role", "PLAYER");
        participantDetail.put("username", username);
        participantDetail.put("userId", userId);

        // 한글은 바이트 코드로 나옴
        // redis 직렬화를 따로 해주면 된다고 함
        // HGETALL room:1:participant:{userId} 모든 필드를 다 조회 가능
        redisTemplate.opsForHash().putAll(key, participantDetail);
    }

}
