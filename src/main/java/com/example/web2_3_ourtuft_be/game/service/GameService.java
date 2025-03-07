package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.redis.service.RoomStatusService;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.websocket.service.WSGameService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final LobbyService lobbyService;
    private final RoomStatusService roomStatusService;
    private final RoomQuizService roomQuizService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WSGameService wsGameService;

    //    public List<Map<String, String>> getQuizList(Long roomId) {
    //        return roomQuizService.getCurrentGameQuizzes(roomId);
    //    }

    public void gameSet(Long roomId) {

        System.out.println("게임 세팅 시작");

        roomStatusService.setGameStatus(roomId, "RUNNING");
        roomStatusService.setCurrentRound(roomId, 0);
        // enableAnswerCheck(roomId); 정답 체크 활성화

        startQuizSending(roomId);
    }

    private void startQuizSending(Long roomId) {

        Room room = lobbyService.findByRoomId(roomId);
        int totalRound = room.getRound();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
        scheduler.scheduleAtFixedRate(
                () -> sendQuiz(roomId, totalRound), 0, room.getTime(), TimeUnit.SECONDS);
    }

    public void sendQuiz(Long roomId, int totalRound) {
        //        System.out.println("sendQuiz 함수 실행 ~~~~~~~~~~~~");
        //        Set<Map<String, String>> quizzes = roomQuizService.getCurrentGameQuizzes(roomId);
        //        int currentRound = roomStatusService.getCurrentRound(roomId);
        //
        //        if (!quizzes.isEmpty() && currentRound < totalRound) {
        //
        //            Map<String, String> quiz = quizzes.get(currentRound);
        //            String question = quiz.get("question");
        //            String hint = quiz.get("hint");
        //
        //            System.out.println("문제 내기 : " + question);
        //            wsGameService.sendQuestion(roomId.toString(), question);
        //            roomStatusService.setCurrentRound(roomId, currentRound + 1);
        //
        //        } else {
        //            stopGame(roomId);
        //        }
    }

    public void stopGame(Long roomId) {
        roomStatusService.setGameStatus(roomId, "WAITING");
        System.out.println("게임 종료!");
    }
}
