package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.redis.dto.ParticipantDto;
import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 타임스탬프로 입장순서를 관리
    public String getParticipantsOrderKey(String roomId) {
        return "room:participants:order:" + roomId;
    }
    public String getfinishUserKey(String roomId) {
        return "room:participants:finish:" +roomId ;
    }

    // 참여자 id,닉네임 관리
    public String getParticipantsInfoKey(String roomId) {
        return "room:participants:info:" + roomId;
    }

    public String getPlayerTotalCountKey(String roomId) {
        return "room:players:count:total:" + roomId;
    }

    public String getPlayerCurrentCountKey(String roomId) {
        return "room:players:count:current:" + roomId;
    }

    // 참여자 준비상태 관리
    public String getReadyStatusKey(String roomId) {
        return "room:readystatus:" + roomId;
    }

    public String getParticipantsScoreKey(String roomId) {
        return "room:participants:score" + roomId;
    }

    public Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    public void addHost(Long roomId, Long playerId, String username) {

        String participantsOrderKey = getParticipantsOrderKey(roomId.toString());
        String participantsInfoKey = getParticipantsInfoKey(roomId.toString());
        String readyStatusKey = getReadyStatusKey(roomId.toString());

        redisTemplate
                .opsForZSet()
                .add(participantsOrderKey, playerId.toString(), getTimeStamp()); // 타임스탬프로 입장 순서 관리
        redisTemplate.opsForHash().put(participantsInfoKey, playerId.toString(), username);
        redisTemplate.opsForHash().put(readyStatusKey, playerId.toString(), "true");
    }

    // 플레이어 준비 상태 토글
    public void togglePlayerReady(Long roomId, Long playerId) {
        String key = getReadyStatusKey(roomId.toString());

        if (redisTemplate.opsForHash().get(key, playerId.toString()).equals("true")) {
            redisTemplate.opsForHash().put(key, playerId.toString(), "false");
        } else {
            redisTemplate.opsForHash().put(key, playerId.toString(), "true");
        }
    }

    public void changeReadyForNewHost(Long roomId, Long playerId) {
        String key = getReadyStatusKey(roomId.toString());

        redisTemplate.opsForHash().put(key, playerId.toString(), "true");
    }

    public void removeParticipant(Long roomId, Long userId) {

        String key = "room:participants:" + roomId;

        redisTemplate.opsForZSet().remove(key, userId);
    }

    // 방에 있는 참가자 리스트 조회
    public List<ParticipantDto> getParticipants(String roomId) {

        List<ParticipantDto> participants = new ArrayList<>();

        String participantsInfoKey = getParticipantsInfoKey(roomId.toString());
        String participantsOrderKey = getParticipantsOrderKey(roomId.toString());
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(participantsInfoKey);

        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            // key와 value를 String으로 캐스팅
            Long playerId = Long.parseLong(entry.getKey().toString());
            String username = (String) entry.getValue();
            Double score = redisTemplate.opsForZSet().score(participantsOrderKey, playerId);

            ParticipantDto participant = ParticipantDto.of(playerId, username, Math.round(score));
            participants.add(participant);
        }

        return participants;
    }

    // 방장이 권한 위임 없이 나갔을 경우 권한 위임 (입장순)
    public String getNextHost(String roomId) {

        String participantsOrderKey = getParticipantsOrderKey(roomId);

        Set<Object> participants = redisTemplate.opsForZSet().range(participantsOrderKey, 0, 2);

        return participants.isEmpty() ? null : (String) participants.iterator().next();
    }

    public List<RoomResponseDto.GetPlayerInRoom> getPlayersInRoom(String roomId) {
        String participantsInfoKey = getParticipantsInfoKey(roomId);
        String readyStatusKey = getReadyStatusKey(roomId);

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(participantsInfoKey);

        return entries.entrySet().stream()
                .map(
                        entry -> {
                            String userId = entry.getKey().toString();
                            String username = entry.getValue().toString();

                            String isReady =
                                    (String) redisTemplate.opsForHash().get(readyStatusKey, userId);

                            return RoomResponseDto.GetPlayerInRoom.of(userId, username, isReady);
                        })
                .collect(Collectors.toList());
    }
}
