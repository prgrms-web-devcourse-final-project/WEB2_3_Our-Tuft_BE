package com.example.web2_3_ourtuft_be.quiz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.quiz.dto.*;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetCategoryType;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
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
    static List<RegistQuizRequest> createTestData() {

        List<RegistQuizRequest> quizzes = new ArrayList<>();
        String question;
        String hint;
        String answer;

        for (int i = 0; i < 30; i++) {
            question = "문제" + i;
            hint = i + "번 문제 힌트";
            answer = i + "번 문제 답";
            RegistQuizRequest registQuizzesRequest =
                    RegistQuizRequest.builder()
                            .question(question)
                            .hint(hint)
                            .answer(answer)
                            .build();
            quizzes.add(registQuizzesRequest);
        }

        return quizzes;
    }

    @DisplayName("전달받은 데이터로 Quiz세트 저장 , 매핑된 Quiz 저장")
    @Test
    void createQuizSet() {
        // given
        List<RegistQuizRequest> quizzes = createTestData();

        Long creatorId = 222L;
        RegistQuizSetAndQuizzesRequest requestData =
                RegistQuizSetAndQuizzesRequest.builder()
                        .quizSetName("testQuizSet")
                        .quizSetType(QuizSetType.OX)
                        .quizSetCategoryType(QuizSetCategoryType.ANIMAL)
                        .quizzes(quizzes)
                        .build();
        // when
        RegistQuizSetAndQuizzesResponse savedQuizSet =
                quizService.registQuizSet(creatorId, requestData);

        // then
        assertThat(savedQuizSet).isNotNull();
        assertEquals(30, savedQuizSet.getQuizzes().size()); // 퀴즈 개수 검증
    }

    @DisplayName("요청받은 QuizSet 데이터로 QuizSet객체를 생성한다.")
    @Test
    void insertQuizSet() {
        // given
        Long creatorId = 222L;
        RegistQuizSetAndQuizzesRequest request =
                RegistQuizSetAndQuizzesRequest.builder()
                        .quizSetName("테스트세트")
                        .quizSetType(QuizSetType.OX)
                        .quizSetCategoryType(QuizSetCategoryType.ANIMAL)
                        .build();

        // when
        QuizSet newQuizSet = quizService.createQuizSet(creatorId, request);

        // then
        assertThat(newQuizSet).isNotNull();
        assertThat(newQuizSet.getQuizSetName()).isEqualTo(request.getQuizSetName());
        // 반환시 Enum 값으로 넣어줘야 할지 그냥 String 으로 넣어줘야 할지
        assertThat(newQuizSet.getQuizSetType()).isEqualTo("OX");
        assertThat(newQuizSet.getQuizSetRunCnt()).isEqualTo(0);
        assertNotNull(newQuizSet.getId());
    }

    @DisplayName("퀴즈셋 삭제 시 관련된 퀴즈도 함께 삭제된다.")
    @Test
    void deleteQuizSet() {

        List<RegistQuizRequest> testQuizzes = createTestData();

        Long creatorId = 222L;

        RegistQuizSetAndQuizzesRequest newQuizSet =
                RegistQuizSetAndQuizzesRequest.builder()
                        .quizSetName("테스트퀴즈세트")
                        .quizSetType(QuizSetType.SPEED)
                        .quizSetCategoryType(QuizSetCategoryType.ANIMAL)
                        .quizzes(testQuizzes)
                        .build();

        RegistQuizSetAndQuizzesResponse registQuizSetAndQuizzesResponse =
                quizService.registQuizSet(creatorId, newQuizSet);

        // when
        quizService.deleteQuizSetAndQuizzes(registQuizSetAndQuizzesResponse.getQuizSetId());

        // then
        assertTrue(
                quizSetRepository
                        .findById(registQuizSetAndQuizzesResponse.getQuizSetId())
                        .isEmpty());
        assertThat(quizRepository.existsByQuizSetId(registQuizSetAndQuizzesResponse.getQuizSetId()))
                .isFalse();
    }

    @DisplayName("존재하지 않는 퀴즈 삭제시 예외 발생")
    @Test
    void deleteQuizSetFailTestWithNoneExistId() {

        Long noneExistId = 999L;

        // when & then
        assertThrows(
                NotFoundException.class,
                () -> {
                    quizService.deleteQuizSetAndQuizzes(noneExistId);
                });
    }


    @DisplayName("요청받은 퀴즈세트, 퀴즈목록으로 업데이트 한다.  ")
    @Test
    void testUpdateQuizSetandQuizzes() {
        // given
        QuizSet quizSet =
                QuizSet.builder()
                        .creatorId(10L)
                        .quizSetName("Old Name")
                        .quizSetCategoryType("ANIMAL")
                        .quizSetType("OX")
                        .quizSetRunCnt(0)
                        .build();

        QuizSet savedQuizSet = quizSetRepository.save(quizSet);

        Quiz quiz1 =
                Quiz.builder()
                        .quizSetId(quizSet.getId())
                        .question("Old Question 1")
                        .hint("Old Hint 1")
                        .answer("Old Answer 1")
                        .build();
        Quiz quiz2 =
                Quiz.builder()
                        .quizSetId(quizSet.getId())
                        .question("Old Question 2")
                        .hint("Old Hint 2")
                        .answer("Old Answer 2")
                        .build();
        quizRepository.saveAll(List.of(quiz1, quiz2));

        UpdateQuizRequest updatedQuiz1 =
                UpdateQuizRequest.builder()
                        .quizId(quiz1.getId())
                        .question("New Question 1")
                        .hint("New Hint 1")
                        .answer("New Answer 1")
                        .build();

        quizRepository.findAllByQuizSetId(savedQuizSet.getId()).orElseThrow();

        UpdateQuizSetandQuizzesRequest request =
                UpdateQuizSetandQuizzesRequest.builder()
                        .quizSetName("New Name")
                        .quizSetType(QuizSetType.SPEED)
                        .quizzes(List.of(updatedQuiz1))
                        .build();

        // when
        quizService.updateQuizSetAndQuizzes(savedQuizSet.getId(), request);

        // then
        QuizSet updatedQuizSet = quizSetRepository.findById(savedQuizSet.getId()).orElseThrow();
        assertEquals("New Name", updatedQuizSet.getQuizSetName());
        assertEquals("SPEED", updatedQuizSet.getQuizSetType());

        Quiz updatedQuiz = quizRepository.findById(quiz1.getId()).orElseThrow();
        assertEquals("New Question 1", updatedQuiz.getQuestion());
        assertEquals("New Answer 1", updatedQuiz.getAnswer());
        assertEquals("New Hint 1", updatedQuiz.getHint());

        List<Quiz> remainingQuizzes = quizRepository.findByQuizSetId(savedQuizSet.getId());
        assertEquals(1, remainingQuizzes.size());
        assertEquals(quiz1.getId(), remainingQuizzes.get(0).getId());
    }
}
