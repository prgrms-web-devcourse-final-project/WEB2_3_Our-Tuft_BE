package com.example.web2_3_ourtuft_be.websocket.service;

import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WSGameService {

    private final RoomQuizService roomQuizService;

    public void gameSetting(String roomId, SimpMessageHeaderAccessor headerAccessor) {
        if (roomQuizService.checkQuizIds(roomId)
        ) {

        }
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
