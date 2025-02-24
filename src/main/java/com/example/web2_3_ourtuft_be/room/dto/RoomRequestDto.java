package com.example.web2_3_ourtuft_be.room.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequestDto {
    private String roomName;
    private boolean disclosure;
    private int password;
    private int round;
    private QuizSetType quizType;
    private int time;
    private int participant;
}
