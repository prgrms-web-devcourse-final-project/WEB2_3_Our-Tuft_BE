package com.example.web2_3_ourtuft_be.redis.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomSettingsDto {
    private String title;
    private boolean isPublic;
    private String password;
    private int maxPlayers;
    private int rounds;
    private int timeLimit;

    @Builder
    public static RoomSettingsDto of(
            String title,
            boolean isPublic,
            String password,
            int maxPlayers,
            int rounds,
            int timeLimit) {
        return RoomSettingsDto.builder()
                .title(title)
                .isPublic(isPublic)
                .password(password)
                .maxPlayers(maxPlayers)
                .rounds(rounds)
                .timeLimit(timeLimit)
                .build();
    }
}
