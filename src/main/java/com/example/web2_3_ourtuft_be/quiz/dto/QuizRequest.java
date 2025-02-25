package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizRequest {
    private String question;
    private String hint;
    private String answer;

    @Builder
    public QuizRequest(String question, String hint, String answer) {
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }

    public static QuizRequest from(Quiz quiz) {
        return QuizRequest.builder()
                .question(quiz.getQuestion())
                .hint(quiz.getHint())
                .answer(quiz.getAnswer())
                .build();
    }
}
