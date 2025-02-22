package com.example.web2_3_ourtuft_be.quiz.service;

import static com.example.web2_3_ourtuft_be.quiz.service.QuizService.createTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.quiz.QuizSetRepository;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizServiceTest {

    @Autowired private QuizService quizService;

    @Autowired private QuizSetRepository quizSetRepository;

    @DisplayName("테스트 데이터를 생성한다.")
    @Test
    void testData() {

        // when
        QuizSetRequest result = createTestData();
        // then
        assertThat(result).isNotNull();
        assertThat(result.getQuizzes().size()).isEqualTo(30);
    }

    @DisplayName("전달받은 데이터로 Quiz세트 저장 , 매핑된 Quiz 저장")
    @Test
    void createQuizSet() {
        // given
        QuizSetRequest requestData = createTestData();

        // when
        QuizSetResponse savedQuizSet = quizService.createQuizSet(requestData);

        // then
        assertThat(savedQuizSet).isNotNull();
        assertEquals(30, savedQuizSet.getQuizzes().size()); // 퀴즈 개수 검증
    }
}
