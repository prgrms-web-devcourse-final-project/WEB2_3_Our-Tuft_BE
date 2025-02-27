package com.example.web2_3_ourtuft_be.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateQuizRequest {

    @NotBlank(message = "문제ID는 필수입니다.") private Long quizId;

    @NotBlank(message = "문제는 필수입니다.") private String question;

    @NotBlank(message = "힌트는 필수입니다.") private String hint;

    @NotBlank(message = "정답은 필수입니다.") private String answer;
}
