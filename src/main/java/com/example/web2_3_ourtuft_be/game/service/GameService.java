package com.example.web2_3_ourtuft_be.game.service;

import com.example.web2_3_ourtuft_be.game.dto.GameResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RedisTemplate<String, String> redisTemplate;

    public List<GameResponse.Scores> getGameScores(String roomId) {
        String key = "game:participants:score:" + roomId;

        Set<String> participants = redisTemplate.opsForZSet().range(key, 0, -1);
        List<GameResponse.Scores> list = new ArrayList<>();

        for (String userId : participants) {
            Double score = redisTemplate.opsForZSet().score(key, userId);
            String username =
                    (String) redisTemplate.opsForHash().get(getUsernameKey(roomId), userId);

            list.add(GameResponse.Scores.from(username, String.valueOf(score != null ? score : 0)));
        }

        return list;
    }

    public String getUsernameKey(String roomId) {
        return "game:participants:info:" + roomId;
    }
}
