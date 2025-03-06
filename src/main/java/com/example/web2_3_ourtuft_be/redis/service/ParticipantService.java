package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.redis.dto.ParticipantDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 타임스탬프로 입장순서를 관리
    public String getParticipantsOrderKey(String roomId) {
        return "room:participants:order" + roomId;
    }

    // 참여자 id,닉네임 관리
    public String getParticipantsInfoKey(String roomId) {
        return "room:participants:info" + roomId;
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

    // 플레이어 추가 (입장 순서와 준비 상태)
    public void addParticipantToRoom(Long roomId, Long playerId, String username) {

        String participantsOrderKey = getParticipantsOrderKey(roomId.toString());
        String participantsInfoKey = getParticipantsInfoKey(roomId.toString());
        String readyStatusKey = getReadyStatusKey(roomId.toString());

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

    // 플레이어 추가
    public void addParticipantToLobby(String playerId, String username) {

        String participantsKey = "room:participants:lobby";
        String playerInfoKey = getParticipantsInfoKey("lobby");

        redisTemplate
                .opsForZSet()
                .add(participantsKey, playerId, getTimeStamp()); // 타임스탬프로 입장 순서 관리
        redisTemplate
                .opsForHash()
                .put(playerInfoKey, playerId, username); // playerId를 field, username을 value로 저장
    }

    // 플레이어 준비 상태 토글
    public void togglePlayerReady(Long roomId, Long playerId) {
        String key = getReadyStatusKey(roomId.toString());

        // 현재 상태 가져오기
        Object currentStatus = redisTemplate.opsForHash().get(key, playerId);
        boolean isReady = currentStatus != null && Boolean.parseBoolean(currentStatus.toString());

        // 반대 상태로 변경
        redisTemplate.opsForHash().put(key, playerId, !isReady);
    }

    // 방에 있는 참가자 리스트 조회
    public List<ParticipantDto> getParticipants(Long roomId) {

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
    public String getNextHost(Long roomId) {

        String participantsOrderKey = getParticipantsOrderKey(roomId.toString());

        Set<Object> participants = redisTemplate.opsForZSet().range(participantsOrderKey, 0, 2);

        return participants.isEmpty() ? null : (String) participants.iterator().next();
    }

    /*public void removeParticipant(Long roomId, Long userId) {

        String key = "room:participants:" + roomId;

        redisTemplate.opsForZSet().remove(key, userId);
    }*/
}
