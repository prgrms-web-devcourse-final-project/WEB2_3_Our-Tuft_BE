package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.room.dto.RoomResponseDto;
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
    public String getParticipantsOrderKey(Long roomId) {
        return "room:participants:order:" + roomId;
    }

    // 참여자 id,닉네임 관리
    public String getParticipantsInfoKey(Long roomId) {
        return "room:participants:info:" + roomId;
    }

    // 참여자 준비상태 관리
    public String getReadyStatusKey(Long roomId) {
        return "room:readystatus:" + roomId;
    }

    public String getParticipantsScoreKey(Long roomId) {
        return "room:participants:score" + roomId;
    }

    public Long getTimeStamp() {
        return System.currentTimeMillis();
    }

    // 플레이어 추가 (입장 순서와 준비 상태)
    public void addParticipantToRoom(Long roomId, String playerId, String username) {

        String participantsOrderKey = getParticipantsOrderKey(roomId);
        String participantsInfoKey = getParticipantsInfoKey(roomId);
        String readyStatusKey = getReadyStatusKey(roomId);

        redisTemplate
                .opsForZSet()
                .add(participantsOrderKey, playerId, getTimeStamp()); // 타임스탬프로 입장 순서 관리
        redisTemplate
                .opsForHash()
                .put(
                        participantsInfoKey,
                        playerId,
                        username); // playerId를 field, username을 value로 저장
        redisTemplate.opsForHash().put(readyStatusKey, playerId, false); // 입장시 준비 상태 false
    }

    // 플레이어 준비 상태 토글
    public void togglePlayerReady(Long roomId, String playerId) {
        String key = getReadyStatusKey(roomId);

        // 현재 상태 가져오기
        Object currentStatus = redisTemplate.opsForHash().get(key, playerId);
        boolean isReady = currentStatus != null && Boolean.parseBoolean(currentStatus.toString());

        // 반대 상태로 변경
        redisTemplate.opsForHash().put(key, playerId, !isReady);
    }

    // 방에 있는 참가자 리스트 조회
    public Map<String, String> getParticipants(Long roomId) {

        String participantsInfoKey = getParticipantsInfoKey(roomId);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(participantsInfoKey);

        // playerId와 username을 Map 형태로 변환하여 반환
        return entries.entrySet().stream()
                .collect(
                        Collectors.toMap(
                                entry -> (String) entry.getKey(), // playerId (key)
                                entry -> (String) entry.getValue() // username (value)
                                ));
    }

    // 방장이 권한 위임 없이 나갔을 경우 권한 위임 (입장순)
    public String getNextHost(String roomId) {

        String key = "room:participants:" + roomId;

        Set<Object> participants = redisTemplate.opsForZSet().range(key, 0, 2);

        return participants.isEmpty() ? null : (String) participants.iterator().next();
    }

    // 유저가 방을 나감
    // 0명이 되면 방 삭제 함수 호출
    public void removeParticipant(Long roomId, Long userId) {

        String key = "room:participants:" + roomId;

        redisTemplate.opsForZSet().remove(key, userId);
    }

    public List<RoomResponseDto.GetPlayerInRoom> getPlayersInRoom(String roomId) {
        String participantsInfoKey = getParticipantsInfoKey(Long.valueOf(roomId));
        String readyStatusKey = getReadyStatusKey(Long.valueOf(roomId));

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
