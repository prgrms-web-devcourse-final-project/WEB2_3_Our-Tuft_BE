package com.example.web2_3_ourtuft_be.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OXResponseDto {
    private boolean isCorrect;
    private int nexRound;
    private boolean isEnd;

}
