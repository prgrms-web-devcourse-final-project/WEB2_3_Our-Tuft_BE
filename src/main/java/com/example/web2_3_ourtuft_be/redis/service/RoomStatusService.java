package com.example.web2_3_ourtuft_be.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomStatusService {

    private final RedisTemplate<String, String> redisTemplate;

    private String getStatusKey(Long roomId) {
        return "room:status:" + roomId;
    }
    private String getRoundKey(Long roomId) {
        return "room:round:" + roomId;
    }


    // 게임 상태 저장
    public void setGameStatus(Long roomId, String status) {
        redisTemplate.opsForValue().set( getStatusKey(roomId), status);
    }

    // 게임 상태 조회
    public String getGameStatus(Long roomId) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String status = ops.get(getStatusKey(roomId));
        return (status != null) ? status : "WAITING";
    }

    // 현재 라운드 저장
    public void setCurrentRound(Long roomId, int round) {
        redisTemplate.opsForValue().set(getRoundKey(roomId), String.valueOf(round));
    }

    // 현재 라운드 조회
    public int getCurrentRound(Long roomId) {
        String round = redisTemplate.opsForValue().get(getRoundKey(roomId));
        return (round != null) ? Integer.parseInt(round) : 0;
    }


}
