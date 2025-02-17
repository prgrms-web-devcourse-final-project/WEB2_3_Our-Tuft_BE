package com.mockApi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReadyStatusResponseDTO {
  private Long playerId;
  private Long roomId;
  private boolean isReady;
  private String message;
}
