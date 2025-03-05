package com.example.web2_3_ourtuft_be.redis.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.redis.entity.RoomStatus;
import com.example.web2_3_ourtuft_be.redis.repository.RoomStatusRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomSettingService {

    private final RoomStatusRedisRepository roomStatusRedisRepository;

    // 방생성시 RoomStatus를 Redis에 저장
    public void saveRoomStatus(Long roomId, String status) {
        RoomStatus roomStatus = RoomStatus.builder().roomId(roomId).status(status).build();
        roomStatusRedisRepository.save(roomStatus);
    }

    // 게임 시작, 종료시 RoomStatus 변경
    public RoomStatus updateRoomStatus(Long roomId, String newStatus) {
        RoomStatus roomStatus =
                roomStatusRedisRepository
                        .findById(roomId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.ROOM_ID));

        roomStatus.updateStatus(newStatus);

        return roomStatusRedisRepository.save(roomStatus);
    }
}
