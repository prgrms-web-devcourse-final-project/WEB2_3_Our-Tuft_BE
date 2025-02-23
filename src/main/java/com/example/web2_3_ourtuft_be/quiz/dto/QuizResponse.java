package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizResponse {
    private String question;
    private String hint;
    private String answer;

    public static QuizResponse from(Quiz quiz) {
        return QuizResponse.builder()
                .question(quiz.getQuestion())
                .hint(quiz.getHint())
                .answer(quiz.getAnswer())
                .build();
    }
}
