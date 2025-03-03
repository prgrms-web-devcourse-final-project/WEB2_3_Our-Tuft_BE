package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.game.dto.GameInfoResponseDto;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.room.entity.Room;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import com.example.web2_3_ourtuft_be.room.service.RoomService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    private final QuizRepository quizRepository;
    private final RoomService roomService;
    private final LobbyService lobbyService;

    // 플레이 할 게임 정보 가져오기.
    // gameType, round -> DB에서 가져오고, gameTopic은 redis에서 가져오는게 좋을 것 같습니다.

    public GameInfoResponseDto getGameInfo(Long gameId) {
        QuizSetType gameType = roomService.getGameTypeByRoomId(gameId);
        String gameTopic = "2010년 가수 맞추기"; // redis에서 가져오기
        int round = roomService.getRoundByRoomId(gameId); // 진행할 퀴즈의 총 라운드 수.

        return new GameInfoResponseDto(gameType, gameTopic, round);
    }

    @Transactional
    public void startGame(Long roomId, String topic) {
        Room room = lobbyService.findByRoomId(roomId);

        // redis에 상태 저장...

        System.out.println("게임 시작 : " + topic);
    }

    // 특정 방에서 다음 문제 가져오기
    // 미완성..
    @Transactional
    public Quiz getNextQuestion(Long roomId) {
        GameInfoResponseDto gameInfo = getGameInfo(roomId);

        // int currentRound redis에서 가져오기
        int currentRound = 1;

        List<Quiz> quizzes =
                quizRepository
                        .findAllByQuizSetId(roomId)
                        .orElseThrow(
                                () -> new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ_SET));

        if (currentRound >= quizzes.size()) {
            throw new InvalidRequestException(InvalidRequestMessages.NO_MORE_QUIZ);
        }

        Quiz nextQuiz = quizzes.get(currentRound);
        // currentRound +1 시킴

        return nextQuiz;
    }
}
