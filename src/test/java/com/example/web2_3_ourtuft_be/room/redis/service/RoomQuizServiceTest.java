package com.example.web2_3_ourtuft_be.room.redis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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

    @DisplayName("진행할 퀴즈세트를 정한다.")
    @Test
    void testSetQuizSet() {
        // given
        Long quizSetId = 10L;

        // when
        roomQuizService.setQuizSet(roomId, quizSetId);

        // then
        Object o = redisTemplate.opsForValue().get(quisetKey);
        assertThat((Long.parseLong(o.toString())));
    }
}
