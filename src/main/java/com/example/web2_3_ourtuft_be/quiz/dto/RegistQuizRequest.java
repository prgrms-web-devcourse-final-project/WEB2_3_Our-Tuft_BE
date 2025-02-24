package com.example.web2_3_ourtuft_be.quiz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizRequest {

    private String question;
    private String hint;
    private String answer;

    @Builder
    public RegistQuizRequest(String question, String hint, String answer) {
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }
}
