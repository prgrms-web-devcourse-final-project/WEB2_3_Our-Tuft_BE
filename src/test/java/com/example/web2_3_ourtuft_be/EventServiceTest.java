package com.example.web2_3_ourtuft_be;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.web2_3_ourtuft_be.coupon.service.EventService;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class EventServiceTest {

    @Autowired private EventService eventService;

    @Autowired private RedisTemplate<String, String> redisTemplate;
    @Autowired private MemberPointService memberPointService;

    @BeforeEach
    void setUp() {
        redisTemplate.delete("event_count");
        redisTemplate.delete("applied_user");
    }

    @Test
    public void testEventApply() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i + 1;
            executorService.execute(
                    () -> {
                        try {
                            eventService.apply(userId);
                        } finally {
                            latch.countDown();
                        }
                    });
        }
        latch.await();

        String count = redisTemplate.opsForValue().get("event_count");

        int point = memberPointService.getPoint(1L).getPoints();
        System.out.println("획득 포인트: " + point);

        assertEquals("20", count);
    }
}
