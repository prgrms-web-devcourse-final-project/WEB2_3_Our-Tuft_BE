package com.example.web2_3_ourtuft_be.quiz.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizzesWithQuizSetId {

    private Long quizSetId;
    private String question;
    private String hint;
    private String answer;

    @Builder
    public QuizzesWithQuizSetId(Long quizSetId, String question, String hint, String answer) {
        this.quizSetId = quizSetId;
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }

    public static QuizzesWithQuizSetId from(Long quizSetId, RegistQuizRequest quizzes) {
        return QuizzesWithQuizSetId.builder()
                .quizSetId(quizSetId)
                .question(quizzes.getQuestion())
                .hint(quizzes.getHint())
                .answer(quizzes.getAnswer())
                .build();
    }
}
