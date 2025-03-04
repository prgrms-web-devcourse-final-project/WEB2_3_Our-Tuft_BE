package com.example.web2_3_ourtuft_be.quiz.service;

import static com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages.INVALID_QUIZ_COUNT;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.quiz.dto.*;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizSetRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;

    @Transactional
    public void updateQuizSetAndQuizzes(Long quizSetId, UpdateQuizSetandQuizzesRequest request) {

        QuizSet quizSet =
                quizSetRepository
                        .findById(quizSetId)
                        .orElseThrow(
                                () -> new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ_SET));

        quizSet.updateQuizSetName(request.getQuizSetName());
        quizSet.updateQuizSetType(request.getQuizSetType().name());

        Set<Long> updatedQuizIds =
                request.getQuizzes().stream()
                        .map(UpdateQuizRequest::getQuizId)
                        .collect(Collectors.toSet());

        // 기존 퀴즈 목록 가져오기
        List<Quiz> existingQuizzes = quizRepository.findByQuizSetId(quizSetId);
        if (existingQuizzes.isEmpty()) {
            throw new NotFoundException(NotFoundMessages.QUIZZES);
        }

        // 기존 QuizSet에 연결되어 있던 Quiz목록들중 request로 넘어오지 않은 퀴즈들 삭제
        deleteQuizzesNotInUpdatedList(existingQuizzes, updatedQuizIds);

        // 각 Quiz 변경내용 업데이트
        updateQuizzes(request);


    }

    private void updateQuizzes(UpdateQuizSetandQuizzesRequest request) {
        for (UpdateQuizRequest updatedQuiz : request.getQuizzes()) {
            Quiz quiz =
                    quizRepository
                            .findById(updatedQuiz.getQuizId())
                            .orElseThrow(() -> new NotFoundException(NotFoundMessages.QUIZ));

            quiz.updateQuestion(updatedQuiz.getQuestion());
            quiz.updateAnswer(updatedQuiz.getAnswer());
            quiz.updateHint(updatedQuiz.getHint());
        }
    }

    private void deleteQuizzesNotInUpdatedList(
        List<Quiz> existingQuizzes, Set<Long> updatedQuizIds) {

        for (Quiz quiz : existingQuizzes) {

            if (!updatedQuizIds.contains(quiz.getId())) {
                quizRepository.delete(quiz);
            }
        }
    }

    @Transactional
    public void deleteQuizSetAndQuizzes(Long quizSetId) {

        boolean existsByQuizSetId = quizRepository.existsByQuizSetId(quizSetId);

        if (existsByQuizSetId) {
            quizRepository.deleteByQuizSetId(quizSetId);

            quizSetRepository.deleteById(quizSetId);
        } else {
            throw new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ_SET);
        }
    }

    @Transactional
    public RegistQuizSetAndQuizzesResponse registQuizSet(
            Long creatorId, RegistQuizSetAndQuizzesRequest registQuizSetAndQuizzesRequest) {

        QuizSet newQuizSet = createQuizSet(creatorId, registQuizSetAndQuizzesRequest);

        List<Quiz> newQuizzes =
                createQuizList(newQuizSet.getId(), registQuizSetAndQuizzesRequest.getQuizzes());

        List<RegistQuizResponse> registQuizRespons = toQuizResponse(newQuizzes);

        return RegistQuizSetAndQuizzesResponse.from(newQuizSet, registQuizRespons);
    }

    private List<RegistQuizResponse> toQuizResponse(List<Quiz> newQuizzes) {

        return newQuizzes.stream().map(RegistQuizResponse::from).toList();
    }

    public List<Quiz> createQuizList(Long quizSetId, List<RegistQuizRequest> requestQuizzes) {
        if (requestQuizzes.isEmpty()) {
            throw new InvalidRequestException(INVALID_QUIZ_COUNT);
        }

        List<Quiz> quizzes = toQuizEntityList(quizSetId, requestQuizzes);

        return quizRepository.saveAll(quizzes);
    }

    // QuizSet 객체생성
    public QuizSet createQuizSet(Long creatorId, RegistQuizSetAndQuizzesRequest request) {

        QuizSet quizSet =
                QuizSet.builder()
                        .creatorId(creatorId)
                        .quizSetName(request.getQuizSetName())
                        .quizSetCategoryType(request.getQuizSetCategoryType().name())
                        .quizSetType(request.getQuizSetType().name())
                        .quizSetRunCnt(0)
                        .build();
        return quizSetRepository.save(quizSet);
    }


    public List<Quiz> toQuizEntityList(
            Long quizSetId, List<RegistQuizRequest> registRegistQuizRequestList) {
        return registRegistQuizRequestList.stream()
                .map(
                        quiz ->
                                Quiz.builder()
                                        .quizSetId(quizSetId)
                                        .question(quiz.getQuestion())
                                        .hint(quiz.getHint())
                                        .answer(quiz.getAnswer())
                                        .build())
                .toList();
    }

    public List<QuizSetTopicResponse> getQuizSets(QuizSetType quizSetType) {

        List<QuizSet> quizSets =
                quizSetRepository
                        .findAllByQuizSetType(quizSetType.name())
                        .orElseThrow(
                                () -> new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ_SET));

        return quizSets.stream().map(QuizSetTopicResponse::from).toList();
    }
}
