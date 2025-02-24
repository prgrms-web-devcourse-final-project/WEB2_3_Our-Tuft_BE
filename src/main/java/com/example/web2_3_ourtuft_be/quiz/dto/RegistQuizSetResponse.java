package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class RegistQuizSetResponse {
    private Long quizSetId;
    private final List<QuizResponse> quizzes;
    private final String quizSetName;
    private final String quizSetType;

    public static RegistQuizSetResponse from(QuizSet quizSet, List<Quiz> quizzes) {
        List<QuizResponse> quizResponses = quizzes.stream().map(QuizResponse::from).toList();
        return RegistQuizSetResponse.builder()
                .quizSetId(quizSet.getId())
                .quizzes(quizResponses)
                .quizSetName(quizSet.getQuizSetName())
                .quizSetType(quizSet.getQuizSetType())
                .build();
    }
}
