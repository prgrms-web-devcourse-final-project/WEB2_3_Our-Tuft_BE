package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetRequest {

    @NotNull(message = "퀴즈 카테고리 ID는 필수입니다.") private Long quizCategoryId;

    @NotBlank(message = "퀴즈 세트 이름은 필수입니다.") private String quizSetName;

    @NotBlank(message = "퀴즈 세트 타입은 필수입니다.") private QuizSetType quizSetType;

    @Builder
    public RegistQuizSetRequest(Long quizCategoryId, String quizSetName, QuizSetType quizSetType) {
        this.quizCategoryId = quizCategoryId;
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
    }
}
