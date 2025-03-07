package com.example.web2_3_ourtuft_be.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OXResponseDto {
    private Long userId;
    private boolean isCorrect;
}
