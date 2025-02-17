package com.mockApi.api.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameStartResponseDTO {
  private Long roomId;
  private String message;
  private int currentRound;
  private List<PlayerDTO> players;
}
