package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.game.dto.OXFinishDto;
import com.example.web2_3_ourtuft_be.game.dto.OXQuizResponse;
import com.example.web2_3_ourtuft_be.game.dto.OXResponseDto;
import com.example.web2_3_ourtuft_be.game.dto.PlayerScoreDto;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OXQuizService {
    private final ParticipantService participantService;
    private final RedisTemplate<String, String> redisTemplate;
    private final RoomQuizService roomQuizService;
    private final QuizService quizService;
    private final GameService gameService;

    public OXResponseDto checkAnswer(Long userId, Long roomId, int round, String userAnswer) {
        String correctAnswer = getCorrectAnswer(roomId, round);
        boolean isCorrect = correctAnswer.equalsIgnoreCase(userAnswer);
        if (isCorrect) {
            updatePlayerScore(roomId, userId);
        }
        return new OXResponseDto(isCorrect);
    }

    public String getCorrectAnswer(Long roomId, int round) {
        return getCurrentQuiz(roomId, round).getAnswer();
    }

    public OXQuizResponse getQuiz(Long roomId, int round) {
        List<Quiz> quizList = getQuizList(roomId);
        Quiz currentQuiz = quizList.get(round - 1);
        boolean isEnd = quizList.size() == round - 1;
        return new OXQuizResponse(round + 1, currentQuiz.getQuestion(), isEnd);
    }

    public List<Quiz> getQuizList(Long roomId) {
        Long quizSetId = roomQuizService.getQuizSet(roomId);
        return quizService.getQuizList(quizSetId);
    }

    public Quiz getCurrentQuiz(Long roomId, int round) {
        Long quizSetId = roomQuizService.getQuizSet(roomId);
        List<Quiz> quizList = quizService.getQuizList(quizSetId);
        return quizList.get(round - 1);
    }

    public void initializePlayerScores(String roomId) {
        String participantsScoreKey = participantService.getParticipantsScoreKey(roomId);
        String participantsOrderKey = participantService.getParticipantsOrderKey(roomId);

        Set<String> range = redisTemplate.opsForZSet().range(participantsOrderKey, 0, -1);

        if (range != null && !range.isEmpty()) {
            for (String participant : range) {
                redisTemplate.opsForZSet().add(participantsScoreKey, participant, 0);
            }
        }
    }

    // TODO : RestAPI용 함수 웹소켓 구현시 삭제예정
    public void updatePlayerScore(Long roomId, Long userId) {

        String participantsScoreKey = participantService.getParticipantsScoreKey(roomId.toString());

        redisTemplate.opsForZSet().incrementScore(participantsScoreKey, userId.toString(), 1);
    }

    // TODO : RestAPI용 함수 웹소켓 구현시 삭제예정
    public List<PlayerScoreDto> getPlayerScores(Long roomId) {
        List<PlayerScoreDto> playerScores = new ArrayList<>();

        String participantsScoreKey = participantService.getParticipantsScoreKey(roomId.toString());
        String getParticipantsInfoKey =
                participantService.getParticipantsInfoKey(roomId.toString());

        Set<ZSetOperations.TypedTuple<String>> typedTuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(participantsScoreKey, 0, -1);
        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {

            String userId = tuple.getValue();
            int score = tuple.getScore().intValue();
            Object nickName = redisTemplate.opsForHash().get(getParticipantsInfoKey, userId);

            PlayerScoreDto playerScoreDto = new PlayerScoreDto(userId, nickName.toString(), score);

            playerScores.add(playerScoreDto);
        }
        return playerScores;
    }

    public OXFinishDto finish(Long userId, Long roomId) {

        // userId, roomId 를 가지고 해당 유저의 상태를 Finish로 바꿔주세요. (없으면 만들어야댐 initPlayerScores 할때 상태 key
        // value

        // 모든 유저가 Finish 인것을 확인한다면
        boolean gameOver = true;
        if (gameOver) {
            List<PlayerScoreDto> playerScores = getPlayerScores(roomId);
            gameService.stopGame(roomId);
            return new OXFinishDto(gameOver, playerScores);
        }

        return new OXFinishDto(gameOver, null);
    }
}
