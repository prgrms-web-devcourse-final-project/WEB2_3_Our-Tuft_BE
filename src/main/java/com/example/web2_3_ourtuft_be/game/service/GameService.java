package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.redis.service.RoomStatusService;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final SimpMessagingTemplate messagingTemplate;
    private final QuizRepository quizRepository;
    private final LobbyService lobbyService;
    private final RoomStatusService roomStatusService;

    // TODO: roomId로 redis에서 quiz 가져오기
    //    public List<Quiz> getQuizList(Long roomId){
    //
    //    }

    @Transactional
    public void startGame(Long roomId, Long quizSetId) {

        Room room = lobbyService.findByRoomId(roomId);

        // 레디스에서 List 가져옴
        List<Quiz> quizzes =
                quizRepository
                        .findAllByQuizSetId(quizSetId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ));

        room.startGame();

        // TODO: redis에 topic 저장...

        System.out.println("게임 시작 : ");
        roomStatusService.setGameStatus(roomId,"RUNNING");
        sendQuiz();
    }

    @Scheduled(fixedRate = 30000)
    public void sendQuiz() {

        //        if (room.isGameRunning() || quizzes == null) return;
        //
        //        if (room.getCurrentRound() >= quizzes.size()) {
        //            messagingTemplate.convertAndSend("/topic/room/" + room.getId(), "GAME_OVER");
        //            stopGame(room);
        //            return;
        //        }
        //
        //        Quiz nextQuiz = quizzes.get(room.getCurrentRound()); // TODO: 예외처리
        //        messagingTemplate.convertAndSend("/topic/room/" + room.getId(), nextQuiz);
        //
        //        System.out.println("출제된 문제: " + nextQuiz);
        //        room.nextRound();
    }

    public void stopGame(Room room) {

        room.endGame();
        System.out.println("게임 종료!");
    }

    // TODO: 응답자 확인 - 누가 몇문제 맞췄는지
}
