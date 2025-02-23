package com.example.web2_3_ourtuft_be.quiz.dto;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RegistQuizSetRequest {

    private final String creatorId;
    private final List<QuizRequest> quizzes;
    private final String quizSetName;
    private final QuizSetType quizSetType;
}
