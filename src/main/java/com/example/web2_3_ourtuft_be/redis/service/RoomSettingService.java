package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.redis.dto.RedisRoomSettingsDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomRequestDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomSettingService {

    private final RedisTemplate<String, Object> redisTemplate;

    public String getRoomQuizSetKey(String roomId) {
        return "room:settings:" + roomId;
    }

    public void saveRoomSettingsToRedis(Long roomId, RoomRequestDto roomRequestDto) {
        String roomSettingsKey = getRoomQuizSetKey(roomId.toString());

        redisTemplate.opsForHash().put(roomSettingsKey, "title", roomRequestDto.getRoomName());
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "disclosure", String.valueOf(roomRequestDto.isDisclosure()));
        redisTemplate.opsForHash().put(roomSettingsKey, "password", roomRequestDto.getPassword());
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "maxplayers", String.valueOf(roomRequestDto.getMaxUsers()));
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "rounds", String.valueOf(roomRequestDto.getRound()));
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "timelimit", String.valueOf(roomRequestDto.getTime()));
    }

    public RedisRoomSettingsDto getRoomSettingsFromRedis(String roomId) {
        String roomSettingsKey = getRoomQuizSetKey(roomId);

        // Redis에서 방 설정을 가져옵니다.
        Map<Object, Object> settingsMap = redisTemplate.opsForHash().entries(roomSettingsKey);

        // Redis에서 가져온 데이터를 RoomSettingsDto로 변환하여 반환
        return RedisRoomSettingsDto.of(
                (String) settingsMap.get("title"),
                Boolean.parseBoolean((String) settingsMap.get("disclosure")),
                (String) settingsMap.get("password"),
                Integer.parseInt((String) settingsMap.get("maxplayers")),
                Integer.parseInt((String) settingsMap.get("rounds")),
                Integer.parseInt((String) settingsMap.get("timelimit")));
    }
}
