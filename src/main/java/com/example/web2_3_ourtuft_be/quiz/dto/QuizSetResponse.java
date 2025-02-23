package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
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
    public QuizSetResponse(List<QuizResponse> quizzes, QuizSet quizSet) {
        this.quizSetId = quizSet.getId();
        this.quizzes = quizzes;
        this.quizSetName = quizSet.getQuizSetName();
        this.quizSetType = quizSet.getQuizSetType();
    }

    public static QuizSetResponse from(QuizSet quizSet, List<QuizResponse> quizzes) {
        return QuizSetResponse.builder().quizzes(quizzes).quizSet(quizSet).build();
    }
}
