package com.example.web2_3_ourtuft_be.room.redis.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomQuizService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getQuizSetKey(Long roomId) {
        return "room:quizset:" + roomId;
    }

    private String getQuizzesKey(Long quizSetId) {
        return "room:quizzes:" + quizSetId;
    }

    // 퀴즈 세트 설정
    public void setQuizSet(Long roomId, Long quizSetId) {
        redisTemplate.opsForValue().set(getQuizSetKey(roomId), quizSetId);
    }

    // 퀴즈 세트에 문제들 추가
    public void setQuizzes(Long quizSetId, Map<String, String> quizzes) {
        String quizzesKey = getQuizzesKey(quizSetId);
        quizzes.forEach(
                (question, answer) -> {
                    redisTemplate.opsForList().rightPush(quizzesKey, question + ":" + answer);
                });
    }

    // 퀴즈 세트 가져오기
    public Long getQuizSet(Long roomId) {
        return Long.parseLong(redisTemplate.opsForValue().get(getQuizSetKey(roomId)).toString());
    }
}
