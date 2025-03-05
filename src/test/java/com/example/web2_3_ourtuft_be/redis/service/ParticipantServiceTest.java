package com.example.web2_3_ourtuft_be.redis.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class ParticipantServiceTest {

    /*@Autowired private RedisTemplate<String, Object> redisTemplate;

    @Autowired private ParticipantService participantService;

    @AfterEach
    void tearDown() {
        Long roomId = 12345L;
        String participnatRoomKey = "room:participants:" + roomId;
        String readyStatusRoomKey = "room:readystatus:" + roomId;
        redisTemplate.delete(participnatRoomKey);
        redisTemplate.delete(readyStatusRoomKey);
    }

    @DisplayName("플레이어가 참여시 해당하는 participants key에 저장한다. 이때 정렬은 timestamp이다.")
    @Test
    void testAddParticipantWithTimeStamp() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;
        String participnatRoomKey = "room:participants:" + roomId;

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

    @DisplayName("플레이어 참여시 해당 room 플레이어 리스트, 준비상태 저장한다. ")
    @Test
    void testAddParticipant() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;

        // when
        participantService.addParticipant(roomId, playerId);

        // then
        Set<Object> range =
                redisTemplate
                        .opsForZSet()
                        .range(participantService.getParticipantsKey(roomId), 0, -1);
        Object o =
                redisTemplate
                        .opsForHash()
                        .get(participantService.getReadyStatusKey(roomId), playerId);
        assertThat(range).size().isEqualTo(1);
        assertFalse(Boolean.parseBoolean(o.toString()));
    }

    @DisplayName("플레이어 준비상태를 변경한다.")
    @Test
    void test() {
        // given
        Long roomId = 12345L;
        Long playerId = 10L;
        participantService.addParticipant(roomId, playerId);

        // when
        participantService.togglePlayerReady(roomId, playerId);

        // then
        Object o =
                redisTemplate
                        .opsForHash()
                        .get(participantService.getReadyStatusKey(roomId), playerId);
        assertTrue(Boolean.parseBoolean(o.toString()));
    }

    @DisplayName("방에 있는 참가자 리스트 조회")
    @Test
    void testGetParticipants() {
        // given
        Long roomId = 12345L;
        String participnatRoomKey = "room:participants:" + roomId;

        redisTemplate.opsForZSet().add(participnatRoomKey, 10, System.currentTimeMillis());
        redisTemplate.opsForZSet().add(participnatRoomKey, 11, System.currentTimeMillis() + 1);
        redisTemplate.opsForZSet().add(participnatRoomKey, 12, System.currentTimeMillis() + 2);
        redisTemplate.opsForZSet().add(participnatRoomKey, 13, System.currentTimeMillis() + 3);

        // when
        List<String> participants = participantService.getParticipants(roomId);

        // then
        assertThat(participants.size()).isEqualTo(4);
    }*/
}
