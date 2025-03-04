package com.example.web2_3_ourtuft_be.room.redis.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomParticipantRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long getTimeStamp() {

        return System.currentTimeMillis();
    }

    public void addParticipant(Long roomId, Long userId) {

        String key = "room:participants:" + roomId;

        redisTemplate.opsForZSet().add(key, userId, getTimeStamp());
    }

    public List<String> getParticipants(Long roomId) {
        String key = "room:participants:" + roomId;

        Set<Object> participants = redisTemplate.opsForZSet().range(key, 0, -1);

        return participants.stream().map(String::valueOf).toList();
    }

    // 방장이 권한 위임 없이 나갔을 경우 권한 위임 (입장순)
    public String getNextHost(Long roomId) {

        String key = "room:participants:" + roomId;

        Set<Object> participants = redisTemplate.opsForZSet().range(key, 0, 2);

        return participants.isEmpty() ? null : (String) participants.iterator().next();
    }

    public void removeParticipant(Long roomId, Long userId) {

        String key = "room:participants:" + roomId;

        redisTemplate.opsForZSet().remove(key, userId);
    }
}
