package com.example.web2_3_ourtuft_be.quiz.controller;

import static com.example.web2_3_ourtuft_be.quiz.service.QuizService.createTestData;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @RequestMapping("/quizzes/regist")
    public GlobalResponse<QuizSetResponse> registQuizset(
            @RequestBody QuizSetRequest quizeSetRequest) {

        QuizSetResponse resultData = quizService.registQuizSet(quizeSetRequest);
        return GlobalResponse.created(resultData);
    }

    // 테스트 퀴즈세트 생성
    public GlobalResponse<QuizSetResponse> registTestQuizSet() {
        QuizSetRequest testData = createTestData();
        QuizSetResponse resultData = quizService.registQuizSet(testData);
        return GlobalResponse.created(resultData);
    }
}
