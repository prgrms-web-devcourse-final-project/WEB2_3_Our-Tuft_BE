package com.example.web2_3_ourtuft_be.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateQuizRequest {

    @NotBlank(message = "문제ID는 필수입니다.") private final Long quizId;

    @NotBlank(message = "문제는 필수입니다.") private final String question;

    @NotBlank(message = "힌트는 필수입니다.") private final String hint;

    @NotBlank(message = "정답은 필수입니다.") private final String answer;

    @Builder
    public UpdateQuizRequest(Long quizId, String question, String hint, String answer) {
        this.quizId = quizId;
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }
}
