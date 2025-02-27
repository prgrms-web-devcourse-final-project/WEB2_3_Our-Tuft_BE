package com.example.web2_3_ourtuft_be.quiz.service;

import static com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages.INVALID_QUIZ_COUNT;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.quiz.dto.*;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizSetRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;

    public void updateQuizSetAndQuizzes(Long quizSetId, UpdateQuizSetandQuizzesRequest request) {

        QuizSet quizSet =
                quizSetRepository
                        .findById(quizSetId)
                        .orElseThrow(
                                () -> new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ_SET));

        quizSet.updateQuizSetName(request.getQuizSetName());
        quizSet.updateQuizSetType(request.getQuizSetType().name());

        for (UpdateQuizRequest updatedQuiz : request.getQuizzes()) {
            Quiz quiz =
                    quizRepository
                            .findById(updatedQuiz.getQuizId())
                            .orElseThrow(() -> new NotFoundException(NotFoundMessages.QUIZ));

            quiz.updateQuestion(updatedQuiz.getQuestion());
            quiz.updateAnswer(updatedQuiz.getAnswer());
            quiz.updateHint(updatedQuiz.getHint());

            quizRepository.save(quiz);
        }

        quizSetRepository.save(quizSet);
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

    @Transactional
    public List<Quiz> createQuizList(Long quizSetId, List<RegistQuizRequest> requestQuizzes) {
        if (requestQuizzes.isEmpty()) {
            throw new InvalidRequestException(INVALID_QUIZ_COUNT);
        }

        List<Quiz> quizzes = toQuizEntityList(quizSetId, requestQuizzes);

        return quizRepository.saveAll(quizzes);
    }

    // QuizSet 객체생성
    @Transactional
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

    @Transactional
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
}
