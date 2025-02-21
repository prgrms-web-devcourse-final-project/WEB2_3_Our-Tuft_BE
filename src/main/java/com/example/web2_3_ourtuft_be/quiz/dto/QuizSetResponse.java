package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuizSetResponse {
    private final Long quizSetId;
    private final List<QuizResponse> quizzes;
    private final String quizSetName;
    private final QuizType quizSetType;
}
