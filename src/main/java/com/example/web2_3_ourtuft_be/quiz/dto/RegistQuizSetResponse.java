package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetResponse {
    private Long quizSetId;
    private final List<QuizResponse> quizzes;
    private final String quizSetName;
    private final String quizSetType;

    @Builder
    public RegistQuizSetResponse(
            Long quizSetId, List<QuizResponse> quizzes, String quizSetName, String quizSetType) {
        this.quizSetId = quizSetId;
        this.quizzes = quizzes;
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
    }

    public static RegistQuizSetResponse from(QuizSet quizSet, List<QuizResponse> quizzes) {
        return RegistQuizSetResponse.builder()
                .quizSetId(quizSet.getId())
                .quizzes(quizzes)
                .quizSetName(quizSet.getQuizSetName())
                .quizSetType(quizSet.getQuizSetType())
                .build();
    }
}
