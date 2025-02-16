package com.mockApi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameStartRequestDTO {
    private int minPlayers;
    private List<PlayerDTO> players;

}
