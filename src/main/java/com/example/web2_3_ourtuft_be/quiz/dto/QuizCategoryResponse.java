package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class QuizCategoryResponse {

    private Long quizCategoryId;
    private String quizCategoryName;


    @Builder
    public QuizCategoryResponse(QuizCategory quizCategory ) {
        this.quizCategoryId = quizCategory.getId();
        this.quizCategoryName = quizCategory.getQuizCategoryName();
    }

    public static QuizCategoryResponse from(QuizCategory quizCategory) {
        return QuizCategoryResponse.builder()
                .quizCategoryId(quizCategory.getId())
                .quizCategoryName(quizCategory.getQuizCategoryName())
                .build();
    }

}
