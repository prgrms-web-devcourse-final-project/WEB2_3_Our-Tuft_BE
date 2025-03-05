package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.redis.dto.RoomSettingsDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomSettingService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getRoomQuizSetKey(String roomId) {
        return "room:settings:" + roomId;
    }

    public void saveRoomSettings(String roomId, RoomSettingsDto roomSettingsDto) {
        String roomSettingsKey = getRoomQuizSetKey(roomId);

        redisTemplate.opsForHash().put(roomSettingsKey, "title", roomSettingsDto.getTitle());
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "isPublic", String.valueOf(roomSettingsDto.isPublic()));
        redisTemplate.opsForHash().put(roomSettingsKey, "password", roomSettingsDto.getPassword());
        redisTemplate
                .opsForHash()
                .put(
                        roomSettingsKey,
                        "maxPlayers",
                        String.valueOf(roomSettingsDto.getMaxPlayers()));
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "rounds", String.valueOf(roomSettingsDto.getRounds()));
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "timeLimit", String.valueOf(roomSettingsDto.getTimeLimit()));
    }

    public RoomSettingsDto getRoomSettings(String roomId) {
        String roomSettingsKey = getRoomQuizSetKey(roomId);

        // Redis에서 방 설정을 가져옵니다.
        Map<Object, Object> settingsMap = redisTemplate.opsForHash().entries(roomSettingsKey);

        // Redis에서 가져온 데이터를 RoomSettingsDto로 변환하여 반환
        return RoomSettingsDto.of(
                (String) settingsMap.get("title"),
                Boolean.parseBoolean((String) settingsMap.get("isPublic")),
                (String) settingsMap.get("password"),
                Integer.parseInt((String) settingsMap.get("maxPlayers")),
                Integer.parseInt((String) settingsMap.get("rounds")),
                Integer.parseInt((String) settingsMap.get("timeLimit")));
    }
}
