package com.example.web2_3_ourtuft_be.room.redis.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.web2_3_ourtuft_be.room.redis.entity.RoomStatus;
import com.example.web2_3_ourtuft_be.room.redis.repository.RoomStatusRedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoomStatusRedisServiceTest {

    @Autowired private RoomStatusRedisRepository roomStatusRedisRepository;
    @Autowired private RoomStatusRedisService roomStatusRedisService;

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
        assertThat(status).extracting("status").isEqualTo(roomStatus);
    }

    @DisplayName("게임시작, 종료 시 RoomStatus를 변경한다. ")
    @Test
    void testUpdateRoomStatus() {
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
