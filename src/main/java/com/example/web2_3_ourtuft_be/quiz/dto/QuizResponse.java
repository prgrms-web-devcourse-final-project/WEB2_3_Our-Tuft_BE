package com.example.web2_3_ourtuft_be.quiz.dto;

import lombok.Builder;

@Builder
public class QuizResponse {
    private String question;
    private String hint;
    private String answer;
}
