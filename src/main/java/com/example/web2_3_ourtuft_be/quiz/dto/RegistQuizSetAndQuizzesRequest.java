package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetCategoryType;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizSetAndQuizzesRequest {

    @NotBlank(message = "퀴즈 세트 이름은 필수입니다.") private String quizSetName;

    @NotNull(message = "퀴즈 카테고리는 필수입니다.") private QuizSetCategoryType quizSetCategoryType;

    @NotNull(message = "퀴즈 세트 타입은 필수입니다.") private QuizSetType quizSetType;

    @NotEmpty(message = "등록할 퀴즈가 없습니다.") private List<RegistQuizRequest> quizzes;

    @Builder
    public RegistQuizSetAndQuizzesRequest(
            String quizSetName,
            QuizSetCategoryType quizSetCategoryType,
            QuizSetType quizSetType,
            List<RegistQuizRequest> quizzes) {
        this.quizSetName = quizSetName;
        this.quizSetCategoryType = quizSetCategoryType;
        this.quizSetType = quizSetType;
        this.quizzes = quizzes;
    }
}
