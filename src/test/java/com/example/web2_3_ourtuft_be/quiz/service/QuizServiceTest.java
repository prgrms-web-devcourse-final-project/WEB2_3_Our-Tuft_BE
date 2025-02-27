package com.example.web2_3_ourtuft_be.quiz.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizzesWithQuizSetId;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.RegistQuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
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
            RegistQuizRequest registQuizRequest =
                    RegistQuizRequest.builder()
                            .question(question)
                            .hint(hint)
                            .answer(answer)
                            .build();
            quizzes.add(registQuizRequest);
        }

        return quizzes;
    }

    @DisplayName("전달받은 데이터로 Quiz세트 저장 , 매핑된 Quiz 저장")
    @Test
    void createQuizSet() {
        // given
        List<RegistQuizRequest> quizList = createTestData();
        RegistQuizSetRequest requestData =
                RegistQuizSetRequest.builder()
                        .creatorId("testUser")
                        .quizzes(quizList)
                        .quizSetName("testQuizSet")
                        .quizSetType(QuizSetType.OX)
                        .build();
        // when
        RegistQuizSetResponse savedQuizSet = quizService.registQuizSet(requestData);

        // then
        assertThat(savedQuizSet).isNotNull();
        assertEquals(30, savedQuizSet.getQuizzes().size()); // 퀴즈 개수 검증
    }

    @DisplayName("요청받은 QuizSet 데이터로 QuizSet객체를 생성한다.")
    @Test
    void insertQuizSet() {
        // given
        RegistQuizSetRequest request =
                RegistQuizSetRequest.builder()
                        .creatorId("testUser")
                        .quizSetName("테스트세트")
                        .quizSetType(QuizSetType.OX)
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
        List<RegistQuizRequest> testQuizzes =
                List.of(
                        RegistQuizRequest.builder()
                                .question("Question1")
                                .hint("Hint1")
                                .answer("answer1")
                                .build(),
                        RegistQuizRequest.builder()
                                .question("Question2")
                                .hint("Hint2")
                                .answer("answer2")
                                .build(),
                        RegistQuizRequest.builder()
                                .question("Question3")
                                .hint("Hint3")
                                .answer("answer3")
                                .build());
        // when
        List<QuizzesWithQuizSetId> quizzesWithQuizSetId =
                quizService.bindQuizSetId(quizSetId, testQuizzes);
        // then
        assertThat(quizzesWithQuizSetId).isNotEmpty();
        quizzesWithQuizSetId.forEach(quiz -> assertThat(quiz.getQuizSetId()).isEqualTo(quizSetId));
    }

    @DisplayName("퀴즈 개수가 1개 미만일경우에 예외 발생")
    @Test
    void insertQuizListWithNoQuiz() {
        // given
        List<QuizzesWithQuizSetId> emptyList = new ArrayList<>();

        // when & then
        assertThrows(InvalidRequestException.class, () -> quizService.createQuizList(emptyList));
    }

    @DisplayName("퀴즈셋 삭제 시 관련된 퀴즈도 함께 삭제된다.")
    @Test
    void deleteQuizSet() {

        List<RegistQuizRequest> quizzes = createTestData();
        RegistQuizSetRequest newQuizSet =
                RegistQuizSetRequest.builder()
                        .creatorId("testUser")
                        .quizzes(quizzes)
                        .quizSetName("테스트퀴즈세트")
                        .quizSetType(QuizSetType.SPEED)
                        .build();

        RegistQuizSetResponse registQuizSetResponse = quizService.registQuizSet(newQuizSet);

        // when
        quizService.deleteQuizSetAndQuizzes(registQuizSetResponse.getQuizSetId());

        // then
        assertTrue(quizSetRepository.findById(registQuizSetResponse.getQuizSetId()).isEmpty());
        assertThat(quizRepository.existsByQuizSetId(registQuizSetResponse.getQuizSetId()))
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
}
