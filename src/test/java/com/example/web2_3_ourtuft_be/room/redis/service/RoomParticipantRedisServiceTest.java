package com.example.web2_3_ourtuft_be.room.redis.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
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
class RoomParticipantRedisServiceTest {

    @Autowired private RedisTemplate<String, Object> redisTemplate;

    @Autowired private RoomParticipantRedisService roomParticipantRedisService;

    @AfterEach
    void tearDown() {
        String roomKey = "room:participants:12345";
        redisTemplate.delete(roomKey);
    }

    Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    @DisplayName("roomId로 참여자 목록을 가져온다.")
    @Test
    void testGetParticipants() {
        // given
        Long roomId = 12345L;
        String key = "room:participants:" + roomId;
        redisTemplate.opsForZSet().add(key, "user1", getTimeStamp());
        redisTemplate.opsForZSet().add(key, "user2", getTimeStamp());
        redisTemplate.opsForZSet().add(key, "user3", getTimeStamp());

        // when
        List<String> participants = roomParticipantRedisService.getParticipants(roomId);

        // then
        assertThat(participants).isNotNull();
        assertThat(participants.get(0)).isEqualTo("user1");
    }

    @DisplayName("유저가 방에 입장한다.")
    @Test
    void testAddParticipant() {
        // given
        Long roomId = 12345L;
        Long timestamp = System.currentTimeMillis();

        // when
        roomParticipantRedisService.addParticipant(roomId, timestamp);

        // then
        roomParticipantRedisService.getParticipants(roomId);
    }

    @DisplayName("입장순으로 다음 방장이 될 인원을 가져온다.")
    @Test
    void testGetNextHost() {
        // given
        Long roomId = 12345L;
        String key = "room:participants:" + roomId;
        redisTemplate.opsForZSet().add(key, "user1", getTimeStamp());
        redisTemplate.opsForZSet().add(key, "user2", getTimeStamp() + 1);
        redisTemplate.opsForZSet().add(key, "user3", getTimeStamp() + 2);

        redisTemplate.opsForZSet().remove(key, "user1");
        // when
        String nextHost = roomParticipantRedisService.getNextHost(roomId);

        // then
        assertThat(nextHost).isEqualTo("user2");
    }

    @DisplayName("플레이어를 내보낸다.")
    @Test
    void testRemoveParticipant() {
        // given
        Long roomId = 12345L;
        String key = "room:participants:" + roomId;
        redisTemplate.opsForZSet().add(key, 101L, getTimeStamp());
        redisTemplate.opsForZSet().add(key, 102L, getTimeStamp() + 1);
        redisTemplate.opsForZSet().add(key, 103L, getTimeStamp() + 2);

        // when
        roomParticipantRedisService.removeParticipant(roomId, 103L);

        // then
        Set<Object> range = redisTemplate.opsForZSet().range(key, 0, -1);
        assertThat(range).size().isEqualTo(2);
    }
}
