package com.example.web2_3_ourtuft_be.quiz.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizSetResponse {
    private Long quizSetId;
    private final List<QuizResponse> quizzes;
    private final String quizSetName;
    private final String quizSetType;

    @Builder
    public QuizSetResponse(
            Long quizSetId, List<QuizResponse> quizzes, String quizSetName, String quizSetType) {
        this.quizzes = quizzes;
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
    }
}
