package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetCategoryType;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetRequest {

    @NotBlank(message = "퀴즈 세트 이름은 필수입니다.") private String quizSetName;
    @NotBlank(message = "퀴즈 카테고리는 필수입니다.") private QuizSetCategoryType quizSetCategoryType;
    @NotBlank(message = "퀴즈 세트 타입은 필수입니다.") private QuizSetType quizSetType;

    @Builder
    public RegistQuizSetRequest(
            String quizSetName, QuizSetCategoryType quizSetCategoryType, QuizSetType quizSetType) {
        this.quizSetName = quizSetName;
        this.quizSetCategoryType = quizSetCategoryType;
        this.quizSetType = quizSetType;
    }
}
