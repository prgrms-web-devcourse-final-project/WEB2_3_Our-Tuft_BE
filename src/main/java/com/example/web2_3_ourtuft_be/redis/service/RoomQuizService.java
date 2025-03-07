package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.websocket.service.WebSocketService;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomQuizService {

    private final RedisTemplate<String, String> redisTemplate;
    private final QuizRepository quizRepository;
    private final WebSocketService webSocketService;

    private String getRoomQuizSetKey(Long roomId) {
        return "room:quizset:" + roomId;
    }

    private String getQuizzesKey(Long quizSetId, Long quizId) {
        return "quiz:set:" + quizSetId + ":" + quizId;
    }

    private String getQuizKey(Long quizId) {
        return "quiz:" + quizId;
    }

    private String getQuizSetKey(Long quizSetId) {
        return "quizSet:" + quizSetId;
    }

    public void checkQuizIds(String roomId) {
        Long quizSetId = getQuizSet(Long.parseLong(roomId));
        System.out.println(quizSetId);
        if (quizSetId == null) {
            webSocketService.sendGameEvent(roomId, "퀴즈가 등록되지 않았습니다.");
            return;
        }

        String key = getQuizSetKey(quizSetId);

        Boolean exists = redisTemplate.hasKey(key);

        if (!exists) setQuizSet(quizSetId);
    }

    public void setQuizSet(Long quizSetId) {
        List<Quiz> quizzes = quizRepository.findByQuizSetId(quizSetId);

        Set<String> quizIds = new HashSet<>();

        for (Quiz quiz : quizzes) {
            Long quizId = quiz.getId();
            String key = getQuizKey(quizId);

            redisTemplate.opsForHash().put(key, "question", quiz.getQuestion());
            redisTemplate.opsForHash().put(key, "hint", quiz.getHint());
            redisTemplate.opsForHash().put(key, "answer", quiz.getAnswer());

            redisTemplate.expire(key, Duration.ofHours(1));

            quizIds.add(String.valueOf(quizId));
        }

        for (String quizId : quizIds) {
            String key = getQuizSetKey(quizSetId);

            redisTemplate.opsForSet().add(key, quizId);
            redisTemplate.expire(key, Duration.ofHours(1));
        }
        log.info("퀴즈세트 추가 성공");
    }

    // 퀴즈 세트 설정
    public void setQuizSet(Long roomId, Long quizSetId) {
        redisTemplate.opsForValue().set(getRoomQuizSetKey(roomId), String.valueOf(quizSetId));
    }

    // 퀴즈 세트 가져오기
    public Long getQuizSet(Long roomId) {
        String key = getRoomQuizSetKey(roomId);

        String value = redisTemplate.opsForValue().get(key);

        if (value == null) return null;

        return Long.parseLong(value);
    }
}
