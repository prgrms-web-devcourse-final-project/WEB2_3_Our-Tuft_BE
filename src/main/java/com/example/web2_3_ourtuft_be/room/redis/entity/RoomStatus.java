package com.example.web2_3_ourtuft_be.room.redis.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "room:status")
@Getter
public class RoomStatus {

    @Id private Long roomId;
    private String status;

    public void updateStatus(String status) {
        this.status = status;
    }

    @Builder
    public RoomStatus(Long roomId, String status) {
        this.roomId = roomId;
        this.status = status;
    }
}
