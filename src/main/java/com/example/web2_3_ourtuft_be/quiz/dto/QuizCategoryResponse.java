package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
public class QuizCategoryResponse {

    private final Long quizCategoryId;
    private final String quizCategoryName;

    @Builder
    public QuizCategoryResponse(Long quizCategoryId, String quizCategoryName) {
        this.quizCategoryId = quizCategoryId;
        this.quizCategoryName = quizCategoryName;
    }

    public static QuizCategoryResponse from(QuizCategory quizCategory) {
        return QuizCategoryResponse.builder()
                .quizCategoryId(quizCategory.getId())
                .quizCategoryName(quizCategory.getQuizCategoryName())
                .build();
    }
}
