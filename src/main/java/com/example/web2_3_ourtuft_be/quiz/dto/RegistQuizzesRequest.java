package com.example.web2_3_ourtuft_be.quiz.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistQuizzesRequest {

    @Valid @Size(min = 20, max = 30, message = "퀴즈는 20~30개여야 합니다.") private List<QuizRequest> quizzes;

    @Builder
    public RegistQuizzesRequest(List<QuizRequest> quizzes) {
        this.quizzes = quizzes;
    }

    public static RegistQuizzesRequest from(List<QuizRequest> quizzes) {
        return builder().quizzes(quizzes).build();
    }
}
