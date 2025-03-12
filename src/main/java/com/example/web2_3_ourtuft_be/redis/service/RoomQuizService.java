package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.websocket.service.WebSocketService;
import java.time.Duration;
import java.util.*;
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

    public String getRoomQuizSetKey(Long roomId) {
        return "room:quizset:" + roomId;
    }

    private String getQuizzesKey(Long quizSetId, Long quizId) {
        return "quiz:set:" + quizSetId + ":" + quizId;
    }

    public String getQuizKey(Long quizId) {
        return "quiz:" + quizId;
    }

    public String getQuizSetKey(Long quizSetId) {
        return "quizSet:" + quizSetId;
    }

    public String getQuizOrderKey(String roomId) {
        return "quiz:order:" + roomId;
    }

    public String getQuizIdByRoom(String roomId, int index) {
        String key = getQuizOrderKey(roomId);
        return redisTemplate.opsForList().index(key, index);
    }

    public Set<String> getQuizzes(String roomId) {
        String quizSetId = redisTemplate.opsForValue().get(getRoomQuizSetKey(Long.valueOf(roomId)));
        String key = getQuizSetKey(Long.valueOf(Objects.requireNonNull(quizSetId)));

        return redisTemplate.opsForSet().members(key);
    }

    public void setQuiz(String roomId, int totalRound) {
        Set<String> quizSet = getQuizzes(roomId);
        int count = Math.min(quizSet.size(), totalRound);
        List<String> quizList = new ArrayList<>(quizSet);
        Collections.shuffle(quizList);
        List<String> selected = quizList.subList(0, count);
        String quizOrderKey = getQuizOrderKey(roomId);

        redisTemplate.delete(quizOrderKey);
        redisTemplate.opsForList().rightPushAll(quizOrderKey, selected);
    }

    public String getQuizDetail(String objKey, String quizKey) {
        return (String) redisTemplate.opsForHash().get(quizKey, objKey);
    }

    public boolean checkQuizIds(String roomId) {

        Long quizSetId = getQuizSet(Long.parseLong(roomId));
        if (quizSetId == null) {
            webSocketService.sendEvent(roomId, "퀴즈가 등록되지 않았습니다.");
            return false;
        }

        String key = getQuizSetKey(quizSetId);

        Boolean exists = redisTemplate.hasKey(key);

        if (!exists) {
            setQuizSet(quizSetId);
        }
        return true;
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
        webSocketService.sendSystemMessage(String.valueOf(roomId), "퀴즈 선택이 완료되었습니다");
    }

    // 퀴즈 세트 가져오기
    public Long getQuizSet(Long roomId) {
        String key = getRoomQuizSetKey(roomId);

        String value = redisTemplate.opsForValue().get(key);

        if (value == null) return null;

        return Long.parseLong(value);
    }
}
