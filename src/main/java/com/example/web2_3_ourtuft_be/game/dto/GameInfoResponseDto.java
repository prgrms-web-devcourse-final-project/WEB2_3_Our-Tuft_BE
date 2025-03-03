package com.example.web2_3_ourtuft_be.game.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameInfoResponseDto {
    private QuizSetType gameType;
    private String gameTopic;
    private int round;
}
