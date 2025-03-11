package com.example.web2_3_ourtuft_be.game.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OXFinishDto {
    boolean gameOver;
    List<PlayerScoreDto> playerScore;
}
