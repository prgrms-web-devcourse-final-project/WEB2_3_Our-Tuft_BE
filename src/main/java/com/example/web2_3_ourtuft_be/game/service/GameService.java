package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.game.dto.PlayerScoreDto;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import com.example.web2_3_ourtuft_be.redis.service.RoomStatusService;
import com.example.web2_3_ourtuft_be.room.service.LobbyService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final LobbyService lobbyService;
    private final RoomStatusService roomStatusService;
    private final RoomQuizService roomQuizService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ParticipantService participantService;
    private final RedisTemplate<String, String> redisTemplate;

    //    public void gameSet(Long roomId) {
    //
    //        System.out.println("게임 세팅 시작");
    //
    //        roomStatusService.setGameStatus(roomId, "RUNNING");
    //        roomStatusService.setCurrentRound(roomId, 0);
    //
    //        startQuizSending(roomId);
    //    }

    //    private void startQuizSending(Long roomId) {
    //
    //        Room room = lobbyService.findByRoomId(roomId);
    //        int totalRound = room.getRound();
    //
    //        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    //        scheduler.scheduleAtFixedRate(() -> sendQuiz(roomId, totalRound), 0, 5,
    // TimeUnit.SECONDS);
    //    }

    //    public void sendQuiz(Long roomId, int totalRound) {
    //        System.out.println("sendQuiz함수 실행 ~~~~~~~~~~~~");
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
    //            messagingTemplate.convertAndSend("/topic/gameRoom/" + roomId, question);
    //            roomStatusService.setCurrentRound(roomId, currentRound + 1);
    //
    //        } else {
    //            stopGame(roomId);
    //        }
    //    }

    public void stopGame(Long roomId) {
        roomStatusService.setGameStatus(roomId, "WAITING");
        System.out.println("게임 종료!");
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
}
