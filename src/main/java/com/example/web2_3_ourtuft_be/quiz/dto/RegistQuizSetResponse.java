package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetResponse {
    private Long quizSetId;
    private final String cretaorId;
    private final String quizSetName;
    private final String quizSetType;
    private final String quizSetCategoryType;
    private final List<QuizResponse> quizzes;

    @Builder
    public RegistQuizSetResponse(
            Long quizSetId,
            String cretaorId,
            String quizSetName,
            String quizSetType,
            String quizSetCategoryType,
            List<QuizResponse> quizzes) {
        this.quizSetId = quizSetId;
        this.cretaorId = cretaorId;
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
        this.quizSetCategoryType = quizSetCategoryType;
        this.quizzes = quizzes;
    }

    @Builder
    public static RegistQuizSetResponse from(QuizSet quizSet, List<QuizResponse> quizzes) {
        return RegistQuizSetResponse.builder()
                .quizSetId(quizSet.getId())
                .cretaorId(quizSet.getCreatorId())
                .quizSetName(quizSet.getQuizSetName())
                .quizSetType(quizSet.getQuizSetType())
                .quizSetCategoryType(quizSet.getQuizSetCategoryType())
                .quizzes(quizzes)
                .build();
    }
}
