package com.example.web2_3_ourtuft_be.quiz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizSetRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuizServiceTest {

    @Autowired private QuizService quizService;

    @Autowired private QuizSetRepository quizSetRepository;

    @Autowired private QuizRepository quizRepository;

    @DisplayName("테스트 퀴즈데이터를 생성한다.")
    static List<QuizRequest> createTestData() {

        List<QuizRequest> quizzes = new ArrayList<>();
        String question;
        String hint;
        String answer;

        for (int i = 0; i < 30; i++) {
            question = "문제" + i;
            hint = i + "번 문제 힌트";
            answer = i + "번 문제 답";
            QuizRequest quizRequest =
                    QuizRequest.builder().question(question).hint(hint).answer(answer).build();
            quizzes.add(quizRequest);
        }

        return quizzes;
    }

    @DisplayName("전달받은 데이터로 Quiz세트 저장 , 매핑된 Quiz 저장")
    @Test
    void createQuizSet() {
        // given
        List<QuizRequest> quizList = createTestData();
        QuizSetRequest requestData =
                QuizSetRequest.builder()
                        .creatorId("testUser")
                        .quizzes(quizList)
                        .quizSetName("testQuizSet")
                        .quizSetType(QuizType.OX)
                        .build();
        // when
        QuizSetResponse savedQuizSet = quizService.registQuizSet(requestData);

        // then
        assertThat(savedQuizSet).isNotNull();
        assertEquals(30, savedQuizSet.getQuizzes().size()); // 퀴즈 개수 검증
    }

    @DisplayName("요청받은 QuizSet 데이터로 QuizSet객체를 생성한다.")
    @Test
    void insertQuizSet() {
        // given
        QuizSetRequest request =
                QuizSetRequest.builder()
                        .creatorId("testUser")
                        .quizSetName("테스트세트")
                        .quizSetType(QuizType.OX)
                        .build();

        // when
        QuizSet newQuizSet = quizService.createQuizSet(request);

        // then
        assertThat(newQuizSet).isNotNull();
        assertThat(newQuizSet.getQuizSetName()).isEqualTo(request.getQuizSetName());
        // 반환시 Enum 값으로 넣어줘야 할지 그냥 String 으로 넣어줘야 할지
        assertThat(newQuizSet.getQuizSetType()).isEqualTo("OX");
        assertThat(newQuizSet.getQuizSetRunCnt()).isEqualTo(0);
        assertNotNull(newQuizSet.getId());
    }

    @DisplayName("생성된 QuizSet의 Id를 각 퀴즈 데이터 DTO에 바인딩한다. ")
    @Test
    void bindQuizSetID() {
        // given
        Long quizSetId = 100L;
        List<QuizRequest> quizListDTO =
                List.of(
                        QuizRequest.builder()
                                .question("Question1")
                                .hint("Hint1")
                                .answer("answer1")
                                .build(),
                        QuizRequest.builder()
                                .question("Question2")
                                .hint("Hint2")
                                .answer("answer2")
                                .build(),
                        QuizRequest.builder()
                                .question("Question3")
                                .hint("Hint3")
                                .answer("answer3")
                                .build());
        // when
        quizService.bindQuizSetId(quizSetId, quizListDTO);
        // then
        assertThat(quizListDTO).isNotEmpty();
        quizListDTO.forEach(quiz -> assertThat(quiz.getQuizSetId()).isEqualTo(quizSetId));
    }

    @DisplayName("퀴즈 개수가 1개 미만일경우에 예외 발생")
    @Test
    void insertQuizListWithNoQuiz() {
        // given
        List<QuizRequest> request = new ArrayList<>();

        // when & then
        assertThrows(InvalidRequestException.class, () -> quizService.createQuizList(request));
    }

    @DisplayName("퀴즈셋 삭제 시 관련된 퀴즈도 함께 삭제된다.")
    @Test
    void deleteQuizSet() {

        List<QuizRequest> quizzes = createTestData();
        QuizSetRequest newQuizSet =
                QuizSetRequest.builder()
                        .creatorId("testUser")
                        .quizzes(quizzes)
                        .quizSetName("테스트퀴즈세트")
                        .quizSetType(QuizType.SPEED)
                        .build();

        QuizSetResponse quizSetResponse = quizService.registQuizSet(newQuizSet);

        // when
        quizService.deleteQuizSet(quizSetResponse.getQuizSetId());

        // then
        assertTrue(quizSetRepository.findById(quizSetResponse.getQuizSetId()).isEmpty());
        assertTrue(
                quizRepository.findAllByQuizSetId(quizSetResponse.getQuizSetId()).get().isEmpty());
    }

    @DisplayName("존재하지 않는 퀴즈 삭제시 예외 발생")
    @Test
    void deleteQuizSetFailTestWithNoneExistId() {

        Long noneExistId = 999L;

        // when & then
        assertThrows(
                NotFoundException.class,
                () -> {
                    quizService.deleteQuizSet(noneExistId);
                });
    }
}
