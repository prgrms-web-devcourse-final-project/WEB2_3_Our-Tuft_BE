package com.example.web2_3_ourtuft_be.room.redis.service;

import com.example.web2_3_ourtuft_be.redis.service.RoomQuizService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoomQuizServiceTest {
    @Autowired private RedisTemplate<String, Object> redisTemplate;

    @Autowired private RoomQuizService roomQuizService;

    Long roomId = 12345L;
    String quisetKey = "room:quizset:" + roomId;
    String quizzesKey = "room:quizzes:" + roomId;

    @AfterEach
    void tearDown() {

        redisTemplate.delete(quisetKey);
        redisTemplate.delete(quizzesKey);
    }

    @DisplayName("test")
    @Test
    void test() {
        // given
        roomQuizService.getAllQuizzes(1L);

        // when

        // then
    }
}
