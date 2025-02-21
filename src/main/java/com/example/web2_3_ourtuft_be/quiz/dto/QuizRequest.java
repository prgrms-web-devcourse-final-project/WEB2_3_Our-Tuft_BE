package com.example.web2_3_ourtuft_be.quiz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizRequest {

    private String question;
    private String hint;
    private String answer;
}
