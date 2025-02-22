package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizRequest {

    private String question;
    private String hint;
    private String answer;

    public static Quiz to(QuizRequest quizRequest) {
        return Quiz.builder()
                .question(quizRequest.getQuestion())
                .hint(quizRequest.getHint())
                .answer(quizRequest.getAnswer())
                .build();
    }
}
