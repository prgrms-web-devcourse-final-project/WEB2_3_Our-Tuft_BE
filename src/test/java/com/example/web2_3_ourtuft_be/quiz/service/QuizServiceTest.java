package com.example.web2_3_ourtuft_be.quiz.service;

import static com.example.web2_3_ourtuft_be.quiz.service.QuizService.createTestData;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.quiz.QuizSetRepository;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizeSetRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuizServiceTest {
    @DisplayName("테스트 데이터 생성")
    @Test
    void test() {
        QuizeSetRequest  testData = createTestData();
        System.out.println(testData.getQuizzes().get(0).getQuestion());

    }
}
