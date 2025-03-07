package com.example.web2_3_ourtuft_be.room.redis.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.redis.dto.ParticipantDto;
import com.example.web2_3_ourtuft_be.redis.service.ParticipantService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ParticipantServiceTest {

    @Autowired private RedisTemplate<String, Object> redisTemplate;

    @Autowired private ParticipantService participantService;

    @AfterEach
    void tearDown() {
        Long roomId = 12345L;
        String participantInfoKey = getParticipantsInfoKey(roomId.toString());
        String readyStatusRoomKey = getParticipantsOrderKey(roomId.toString());
        String readyStatusKey = getReadyStatusKey(roomId.toString());
        redisTemplate.delete(participantInfoKey);
        redisTemplate.delete(readyStatusRoomKey);
    }

    // 참여자 id,닉네임 관리
    public String getParticipantsInfoKey(String roomId) {
        return "room:participants:info" + roomId;
    }

    // 참여자 준비상태 관리
    public String getReadyStatusKey(String roomId) {
        return "room:readystatus:" + roomId;
    }

    public String getParticipantsOrderKey(String roomId) {
        return "room:participants:order" + roomId;
    }

    @DisplayName("플레이어가 참여시 해당하는 participants key에 저장한다. 이때 정렬은 timestamp이다.")
    @Test
    void testAddParticipantWithTimeStamp() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;
        String participnatRoomKey = "room:participants:info" + roomId;

        // when
        redisTemplate.opsForZSet().add(participnatRoomKey, playerId, System.currentTimeMillis());

        // then
        Set<Object> range = redisTemplate.opsForZSet().range(participnatRoomKey, 0, -1);
        assertThat(range).size().isEqualTo(1);
        assertThat(range.iterator().next()).isEqualTo(10);
    }

    @DisplayName("플레이어가 참여시 readystatus key에 준비상태 false로 저장한다. ")
    @Test
    void testAddReadyStatusWithFalse() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;
        String statusRoomKey = "room:readystatus:" + roomId;

        // when
        redisTemplate.opsForHash().put(statusRoomKey, playerId, false);

        // then
        Object o = redisTemplate.opsForHash().get(statusRoomKey, playerId);
        assertFalse(Boolean.parseBoolean(o.toString()));
    }

    @DisplayName("플레이어 참여시 해당 room 플레이어 리스트, 입장순서, 준비상태 저장한다. ")
    @Test
    void testAddParticipantToRoom() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;
        String userName = "user1";

        // when
        participantService.addParticipantToRoom(roomId, playerId, userName);

        // then
        Set<Object> range =
                redisTemplate.opsForZSet().range(getParticipantsOrderKey(roomId.toString()), 0, -1);
        Object readyStsatus =
                redisTemplate
                        .opsForHash()
                        .get(participantService.getReadyStatusKey(roomId.toString()), playerId);
        Object value =
                redisTemplate.opsForHash().get(getParticipantsInfoKey(roomId.toString()), playerId);
        assertThat(range).size().isEqualTo(1);
        assertFalse(Boolean.parseBoolean(readyStsatus.toString()));
        assertThat(value.toString()).isEqualTo(userName);
    }

    @DisplayName("플레이어 준비상태를 변경한다.")
    @Test
    void testTogglePlayerReady() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;
        String readyStatusKey = getReadyStatusKey(roomId.toString());
        redisTemplate.opsForHash().put(readyStatusKey, playerId, false);

        // when
        participantService.togglePlayerReady(roomId, playerId);

        // then
        Object o =
                redisTemplate
                        .opsForHash()
                        .get(participantService.getReadyStatusKey(roomId.toString()), playerId);
        assertTrue(Boolean.parseBoolean(o.toString()));
    }

    @DisplayName("방에 있는 참가자 리스트 조회")
    @Test
    void testGetParticipants() {
        // given
        Long roomId = 12345L;
        String participnatInfoKey = "room:participants:info" + roomId;
        String participantOrdrKey = "room:participants:order" + roomId;

        redisTemplate.opsForZSet().add(participantOrdrKey, 10L, System.currentTimeMillis());
        redisTemplate.opsForZSet().add(participantOrdrKey, 11L, System.currentTimeMillis() + 1);
        redisTemplate.opsForZSet().add(participantOrdrKey, 12L, System.currentTimeMillis() + 2);
        redisTemplate.opsForZSet().add(participantOrdrKey, 13L, System.currentTimeMillis() + 3);

        redisTemplate.opsForHash().put(participnatInfoKey, 10L, "user1");
        redisTemplate.opsForHash().put(participnatInfoKey, 11L, "user2");
        redisTemplate.opsForHash().put(participnatInfoKey, 12L, "user3");
        redisTemplate.opsForHash().put(participnatInfoKey, 13L, "user4");

        // when
        List<ParticipantDto> participants = participantService.getParticipants(roomId.toString());

        // then
        assertThat(participants.size()).isEqualTo(4);
    }
}
