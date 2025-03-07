package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class OXGameService {
    private static final int COUNTDOWN_TIME = 15;
    private final RedisTemplate<String, String> oxRedisTemplate;
    private final QuizRepository quizRepository;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    // Redis에 저장된 유저 정보
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, SseEmitter>> roomEmitters = new ConcurrentHashMap<>();

    // SSE 구독 (게임 참여)
    public SseEmitter subscribeToRoom(String roomId, Long userId) {
        SseEmitter emitter = new SseEmitter(15 * 60 * 1000L);  // 15분 타임아웃

        emitter.onCompletion(() -> removeUserFromRoom(roomId, userId));
        emitter.onTimeout(() -> removeUserFromRoom(roomId, userId));

        // 방에 유저와 연결된 emitter 추가
        roomEmitters.computeIfAbsent(roomId, key -> new ConcurrentHashMap<>()).put(userId.toString(), emitter);

        return emitter;
    }

    // 퀴즈 시작 및 카운트 다운 실행
    public void startGame(String roomId) {
        List<Quiz> quizList = quizRepository.findByQuizSetId(1L);
        int round = 1;
        for (Quiz quiz : quizList) {
            startQuizCountdown(roomId, quiz, round);
        }
    }

    private void startQuizCountdown(String roomId, Quiz quiz, int round) {
        sendEventToRoom(roomId, "quiz", round + " : " + quiz.getQuestion());
        AtomicInteger timeLeft = new AtomicInteger(COUNTDOWN_TIME);

        // 타이머 실행
        scheduler.scheduleAtFixedRate(() -> {
            int remainingTime = timeLeft.getAndDecrement();
            sendEventToRoom(roomId, "timer", remainingTime);

            if (remainingTime <= 0) {
                showAnswer(roomId, quiz, round);
                scheduler.shutdown();  // 카운트다운이 끝나면 스케줄러 종료
            }
        }, 0, 1, TimeUnit.SECONDS); // 1초마다 실행
    }

    private void showAnswer(String roomId, Quiz quiz, int round) {
        String answer = quiz.getAnswer();
        sendEventToRoom(roomId, "answer", answer);
        scheduler.schedule(() -> {
            startQuizCountdown(roomId, quiz, round + 1);
        }, 3, TimeUnit.SECONDS);
    }

    public void sendEventToRoom(String roomId, String event, Object message) {
        oxRedisTemplate.convertAndSend("room", roomId + ":" + event + ":" + message);
    }

    // 유저를 방에서 제거하는 메서드
    private void removeUserFromRoom(String roomId, Long userId) {
        ConcurrentHashMap<String, SseEmitter> emitters = roomEmitters.get(roomId);
        if (emitters != null) {
            emitters.remove(userId);  // 유저의 SseEmitter를 방에서 제거
        }
    }
}
