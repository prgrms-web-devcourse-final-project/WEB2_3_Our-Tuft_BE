package com.example.web2_3_ourtuft_be.quiz.controller;

import static com.example.web2_3_ourtuft_be.quiz.service.QuizService.createTestData;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizeSetRequest;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    public GlobalResponse<QuizSet> registQuizset(@RequestBody QuizeSetRequest quizeSetRequest) {
        QuizSet resultData = quizService.createQuizSet(quizeSetRequest);
        return GlobalResponse.created(resultData);
    }

    // 테스트 퀴즈세트 생성
    public GlobalResponse<QuizSet> registTestQuizSet() {
        QuizeSetRequest testData = createTestData();
        QuizSet resultData = quizService.createQuizSet(testData);
        return GlobalResponse.created(resultData);
    }
}
