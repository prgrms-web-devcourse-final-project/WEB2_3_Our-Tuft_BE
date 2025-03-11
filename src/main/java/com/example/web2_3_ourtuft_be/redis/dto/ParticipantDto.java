package com.example.web2_3_ourtuft_be.redis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ParticipantDto {
    private Long userId;
    private String nickName;
    private Long timeStamp;

    @Builder
    public ParticipantDto(Long userId, String nickName, Long timeStamp) {
        this.userId = userId;
        this.nickName = nickName;
        this.timeStamp = timeStamp;
    }

    public static ParticipantDto of(Long userId, String nickName, Long timeStamp) {
        return ParticipantDto.builder()
                .userId(userId)
                .nickName(nickName)
                .timeStamp(timeStamp)
                .build();
    }
}
