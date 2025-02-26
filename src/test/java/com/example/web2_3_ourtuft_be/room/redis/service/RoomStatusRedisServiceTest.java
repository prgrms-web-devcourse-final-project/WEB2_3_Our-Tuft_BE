package com.example.web2_3_ourtuft_be.room.redis.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.web2_3_ourtuft_be.room.redis.entity.RoomStatus;
import com.example.web2_3_ourtuft_be.room.redis.repository.RoomStatusRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoomStatusRedisServiceTest {

    @Autowired private RoomStatusRedisRepository roomStatusRedisRepository;

    private RoomStatusRedisService roomStatusRedisService;

    @BeforeEach
    void setUp() {
        roomStatusRedisService = new RoomStatusRedisService(roomStatusRedisRepository);
    }

    @AfterEach
    void tearDown() {
        roomStatusRedisRepository.deleteAll();
    }

    @DisplayName("방생성시 RoomStatus를 Redis에 저장한다.")
    @Test
    void saveRoomStatus() {
        // given
        Long roomId = 20L;
        String roomStatus = "IN_PROGRESS";
        roomStatusRedisService.saveRoomStatus(roomId, roomStatus);

        // when
        RoomStatus status = roomStatusRedisRepository.findById(20L).orElse(null);

        // then
        assertThat(status).isNotNull();
        assertThat(status).extracting("roomStatus").isEqualTo(roomStatus);
    }

    @DisplayName("게임시작, 종료 시 RoomStatus를 변경한다. ")
    @Test
    void test() {
        // given
        Long roomId = 20L;
        String roomStatus = "IN_PROGRESS";
        String newRoomStatus = "OPEN";
        roomStatusRedisService.saveRoomStatus(roomId, roomStatus);

        // when
        RoomStatus changedRoomStatus =
                roomStatusRedisService.updateRoomStatus(roomId, newRoomStatus);

        // then
        assertThat(changedRoomStatus.getStatus()).isEqualTo(newRoomStatus);
    }
}
