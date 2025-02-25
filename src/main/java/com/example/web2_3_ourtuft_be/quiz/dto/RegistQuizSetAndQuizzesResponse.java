package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetAndQuizzesResponse {
    private Long quizSetId;
    private final Long cretaorId;
    private final String quizSetName;
    private final String quizSetType;
    private final String quizSetCategoryType;
    private final List<QuizResponse> quizzes;

    @Builder
    public RegistQuizSetAndQuizzesResponse(
            Long quizSetId,
            Long cretaorId,
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

    public static RegistQuizSetAndQuizzesResponse from(
            QuizSet quizSet, List<QuizResponse> quizzes) {
        return RegistQuizSetAndQuizzesResponse.builder()
                .quizSetId(quizSet.getId())
                .cretaorId(quizSet.getCreatorId())
                .quizSetName(quizSet.getQuizSetName())
                .quizSetType(quizSet.getQuizSetType())
                .quizSetCategoryType(quizSet.getQuizSetCategoryType())
                .quizzes(quizzes)
                .build();
    }
}
