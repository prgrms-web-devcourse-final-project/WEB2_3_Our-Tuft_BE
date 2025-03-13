package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
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

    public void deleteRoomSettingToRedis(String roomId) {
        String roomSettingKey = getRoomQuizSetKey(roomId);

        redisTemplate.delete(roomSettingKey);
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
        redisTemplate
                .opsForHash()
                .put(roomSettingsKey, "gameType", String.valueOf(roomRequestDto.getGameType()));
    }

    public RoomRequestDto getRoomSettingsFromRedis(String roomId) {
        String roomSettingsKey = getRoomQuizSetKey(roomId);

        // Redis에서 방 설정을 가져옵니다.
        Map<Object, Object> settingsMap = redisTemplate.opsForHash().entries(roomSettingsKey);

        QuizSetType setType = QuizSetType.SPEED;
        if (settingsMap.get("gameType").equals("OX")) setType = QuizSetType.OX;
        if (settingsMap.get("gameType").equals("CATCHMIND")) setType = QuizSetType.CATCHMIND;

        return new RoomRequestDto(
                (String) settingsMap.get("title"),
                Boolean.parseBoolean((String) settingsMap.get("disclosure")),
                (String) settingsMap.get("password"),
                Integer.parseInt((String) settingsMap.get("rounds")),
                setType,
                Integer.parseInt((String) settingsMap.get("timelimit")),
                8);
    }
}
