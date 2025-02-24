package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetRequest {

    private final String creatorId;
    private final List<RegistQuizRequest> quizzes;
    private final String quizSetName;
    private final QuizSetType quizSetType;

    @Builder
    public RegistQuizSetRequest(
            String creatorId,
            List<RegistQuizRequest> quizzes,
            String quizSetName,
            QuizSetType quizSetType) {
        this.creatorId = creatorId;
        this.quizzes = quizzes;
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
    }
}
