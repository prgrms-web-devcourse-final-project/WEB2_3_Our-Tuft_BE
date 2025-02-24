package com.example.web2_3_ourtuft_be.quiz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizRequest {

    private Long quizSetId;
    private String question;
    private String hint;
    private String answer;


    public QuizRequest(String question, String hint, String answer) {
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }

    public void setQuizSetId(Long quizSetId) {
        this.quizSetId = quizSetId;
    }
}
