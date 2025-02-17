package com.mockApi.api.dto;

import lombok.Getter;

@Getter
public class RoomResponseDto {
  private final Long id;
  private final String gameType;
  private final String roomName;
  private final boolean disclosure;
  private final int timeLimit;
  private final int peopleEntering;
  private final int round;
  private final String host;

  public RoomResponseDto(
      Long id,
      String gameType,
      String roomName,
      boolean disclosure,
      int timeLimit,
      int peopleEntering,
      int round,
      String host) {
    this.id = id;
    this.gameType = gameType;
    this.roomName = roomName;
    this.disclosure = disclosure;
    this.timeLimit = timeLimit;
    this.peopleEntering = peopleEntering;
    this.round = round;
    this.host = host;
  }
}
