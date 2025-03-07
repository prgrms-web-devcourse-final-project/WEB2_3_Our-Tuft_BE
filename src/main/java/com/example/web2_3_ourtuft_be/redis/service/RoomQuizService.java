package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomQuizService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final QuizRepository quizRepository;

    private String getRoomQuizSetKey(Long roomId) {
        return "room:quizset:" + roomId;
    }

    private String getQuizzesKey(Long quizSetId, Long quizId) {
        return "quiz:set:" + quizSetId + ":" + quizId;
    }

    public List<Map<String, String>> getCurrentGameQuizzes(Long roomId) {

        Long quizSetId = getQuizSet(roomId);

        return getAllQuizzes(quizSetId);
    }

    // 퀴즈 세트 설정
    public void setQuizSet(Long roomId, Long quizSetId) {
        redisTemplate.opsForValue().set(getRoomQuizSetKey(roomId), quizSetId);
    }

    // 퀴즈 세트 가져오기
    public Long getQuizSet(Long roomId) {
        return Long.parseLong(
                redisTemplate.opsForValue().get(getRoomQuizSetKey(roomId)).toString());
    }

    public List<Map<String, String>> getAllQuizzes(Long quizSetId) {
        List<Map<String, String>> quizzes = new ArrayList<>();

        // Redis에서 해당 quizSetId의 모든 키 조회
        Set<String> quizKeys = redisTemplate.keys("quiz:set:" + quizSetId + ":*");

        if (quizKeys != null && !quizKeys.isEmpty()) {
            for (String quizKey : quizKeys) {
                Map<Object, Object> quizData = redisTemplate.opsForHash().entries(quizKey);
                quizzes.add(
                        quizData.entrySet().stream()
                                .collect(
                                        Collectors.toMap(
                                                e -> (String) e.getKey(),
                                                e -> (String) e.getValue())));
            }
            return quizzes;
        }

        // Redis에 없으면 DB에서 가져와서 캐싱
        List<Quiz> dbQuizzes = quizRepository.findByQuizSetId(quizSetId);
        for (Quiz quiz : dbQuizzes) {
            String quizKey = getQuizzesKey(quizSetId, quiz.getId());

            redisTemplate.opsForHash().put(quizKey, "question", quiz.getQuestion());
            redisTemplate.opsForHash().put(quizKey, "hint", quiz.getHint());
            redisTemplate.opsForHash().put(quizKey, "answer", quiz.getAnswer());

            redisTemplate.expire(quizKey, Duration.ofHours(1)); // TTL 설정

            Map<String, String> quizData = new HashMap<>();
            quizData.put("question", quiz.getQuestion());
            quizData.put("hint", quiz.getHint());
            quizData.put("answer", quiz.getAnswer());
            quizzes.add(quizData);
        }

        return quizzes;
    }
}
