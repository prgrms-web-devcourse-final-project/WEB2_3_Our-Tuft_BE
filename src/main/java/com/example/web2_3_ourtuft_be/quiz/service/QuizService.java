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
    public RegistQuizSetResponse registQuizSet(
            String creatorId,
            RegistQuizSetRequest registQuizSetRequest,
            RegistQuizzesRequest registQuizzesRequest) {

        QuizSet newQuizSet = createQuizSet(creatorId, registQuizSetRequest);

        List<Quiz> newQuizzes =
                createQuizList(newQuizSet.getId(), registQuizzesRequest.getQuizzes());

        List<QuizResponse> quizResponses = toQuizResponse(newQuizzes);

        return RegistQuizSetResponse.from(newQuizSet, quizResponses);
    }

    private static List<QuizResponse> toQuizResponse(List<Quiz> newQuizzes) {

        return newQuizzes.stream().map(QuizResponse::from).toList();
    }

    @Transactional
    public List<Quiz> createQuizList(Long quizSetId, List<QuizRequest> requestQuizzes) {
        if (requestQuizzes.isEmpty()) {
            throw new InvalidRequestException(INVALID_QUIZ_COUNT);
        }

        List<Quiz> quizzes = toQuizEntityList(quizSetId, requestQuizzes);

        return quizRepository.saveAll(quizzes);
    }

    // QuizSet 객체생성
    @Transactional
    public QuizSet createQuizSet(String creatorId, RegistQuizSetRequest request) {

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

    public List<Quiz> toQuizEntityList(Long quizSetId, List<QuizRequest> registQuizRequestList) {
        return registQuizRequestList.stream()
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
