package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizSetSummaryResponse {
    private final Long quizSetId;
    private final String quizSetName;

    @Builder
    public QuizSetSummaryResponse(Long quizSetId, String quizSetName) {
        this.quizSetId = quizSetId;
        this.quizSetName = quizSetName;
    }

    public static QuizSetSummaryResponse from(QuizSet quizSet) {
        return QuizSetSummaryResponse.builder()
                .quizSetId(quizSet.getId())
                .quizSetName(quizSet.getQuizSetName())
                .build();
    }
}
