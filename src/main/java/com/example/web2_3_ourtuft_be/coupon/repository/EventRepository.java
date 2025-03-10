package com.example.web2_3_ourtuft_be.coupon.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventRepository {

    private final RedisTemplate<String, Long> redisTemplate;

    public Long increment() {
        return redisTemplate.opsForValue().increment("event_count");
    }

    public void decrement() {
        redisTemplate.opsForValue().decrement("event_count");
    }

    public boolean checkDuplicateApply(Long userId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember("applied_user", userId));
    }

    public Long add(Long userId) {
        return redisTemplate.opsForSet().add("applied_user", userId);
    }
}
