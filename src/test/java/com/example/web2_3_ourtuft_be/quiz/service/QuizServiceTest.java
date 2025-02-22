package com.example.web2_3_ourtuft_be.quiz.service;

import static com.example.web2_3_ourtuft_be.quiz.service.QuizService.createTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizSetRepository;
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
        QuizSetResponse savedQuizSet = quizService.registQuizSet(requestData);

        // then
        assertThat(savedQuizSet).isNotNull();
        assertEquals(30, savedQuizSet.getQuizzes().size()); // 퀴즈 개수 검증
    }

    @DisplayName("요청받은 QuizSet 데이터를 DB에 저장한다.")
    @Test
    void insertQuizSet() {
        // given
        QuizSetRequest request = createTestData();

        // when
        QuizSet newQuizSet = quizService.insertQuizSet(request);

        // then
        assertThat(newQuizSet).isNotNull();
        assertThat(newQuizSet.getQuizSetName()).isEqualTo(request.getQuizSetName());
        // 반환시 Enum 값으로 넣어줘야 할지 그냥 String 으로 넣어줘야 할지
        assertThat(newQuizSet.getQuizSetType()).isEqualTo("OX");
        assertThat(newQuizSet.getQuizSetRunCnt()).isEqualTo(0);
        assertNotNull(newQuizSet.getId());
    }
}
