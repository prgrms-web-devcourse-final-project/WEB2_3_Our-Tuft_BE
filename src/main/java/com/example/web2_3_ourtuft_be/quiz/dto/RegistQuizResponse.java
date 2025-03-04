package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizResponse {
    private final String question;
    private final String hint;
    private final String answer;

    @Builder
    public RegistQuizResponse(String question, String hint, String answer) {
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }

    public static RegistQuizResponse from(Quiz quiz) {
        return RegistQuizResponse.builder()
                .question(quiz.getQuestion())
                .hint(quiz.getHint())
                .answer(quiz.getAnswer())
                .build();
    }
}
