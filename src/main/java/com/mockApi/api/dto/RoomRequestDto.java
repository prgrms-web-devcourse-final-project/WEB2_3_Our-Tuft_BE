package com.mockApi.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoomRequestDto {
    private String gameType;
    private String roomName;
    private boolean disclosure;
    private String roomPassword;
    private int peopleEntering;
    private int round;
    private int gameTime;
}
