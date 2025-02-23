package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
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
    public RegistQuizSetResponse(List<QuizResponse> quizzes, QuizSet quizSet) {
        this.quizSetId = quizSet.getId();
        this.quizzes = quizzes;
        this.quizSetName = quizSet.getQuizSetName();
        this.quizSetType = quizSet.getQuizSetType();
    }

    public static RegistQuizSetResponse from(QuizSet quizSet, List<Quiz> quizzes) {
        List<QuizResponse> quizResponses = quizzes.stream().map(QuizResponse::from).toList();
        return RegistQuizSetResponse.builder().quizzes(quizResponses).quizSet(quizSet).build();
    }
}
