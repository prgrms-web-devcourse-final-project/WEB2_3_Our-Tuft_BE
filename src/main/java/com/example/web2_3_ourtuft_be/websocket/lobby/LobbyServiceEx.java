package com.example.web2_3_ourtuft_be.websocket.lobby;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LobbyServiceEx {

    private final RedisTemplate<String, Object> redisTemplate;
}
