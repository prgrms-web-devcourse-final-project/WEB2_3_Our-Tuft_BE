package com.example.web2_3_ourtuft_be.room.redis.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final RedisTemplate<String, Object> redisTemplate;

    public String getParticipantsKey(Long roomId) {
        return "room:participants:" + roomId;
    }

    public String getReadyStatusKey(Long roomId) {
        return "room:readystatus:" + roomId;
    }

    public String getScoreKey(Long roomId) {
        return "room:score:" + roomId;
    }

    public Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    // 플레이어 추가 (입장 순서와 준비 상태)
    public void addParticipant(Long roomId, Long playerId) {

        String participantsKey = getParticipantsKey(roomId);

        redisTemplate
                .opsForZSet()
                .add(participantsKey, playerId, getTimeStamp()); // 타임스탬프로 입장 순서 관리
        redisTemplate
                .opsForHash()
                .put(getReadyStatusKey(roomId), playerId, false); // 입장시 준비 상태 false
    }

    // 플레이어 준비 상태 토글
    public void togglePlayerReady(Long roomId, Long playerId) {
        String key = getReadyStatusKey(roomId);

        // 현재 상태 가져오기
        Object currentStatus = redisTemplate.opsForHash().get(key, playerId);
        boolean isReady = currentStatus != null && Boolean.parseBoolean(currentStatus.toString());

        // 반대 상태로 변경
        redisTemplate.opsForHash().put(key, playerId, !isReady);
    }

    // 방에 있는 참가자 리스트 조회
    public List<String> getParticipants(Long roomId) {

        String participantsKey = getParticipantsKey(roomId);
        Set<Object> range = redisTemplate.opsForZSet().range(participantsKey, 0, -1);

        return range.stream().map(String::valueOf).toList();
    }
}
