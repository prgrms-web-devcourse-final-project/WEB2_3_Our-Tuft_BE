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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;

    @Transactional
    public List<QuizSetSummaryResponse> getQuizSetList(QuizSetType quizSetType) {

        List<QuizSet> quizSets = quizSetRepository.findAllByQuizSetType(quizSetType.name());

        return quizSets.stream().map(QuizSetSummaryResponse::from).toList();
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
    public RegistQuizSetResponse registQuizSet(RegistQuizSetRequest request) {

        QuizSet newQuizSet = createQuizSet(request);

        List<RegistQuizRequest> quizzes = request.getQuizzes();

        List<QuizzesWithQuizSetId> quizzesWithQuizSetId =
                bindQuizSetId(newQuizSet.getId(), quizzes);

        List<Quiz> newQuizzes = createQuizList(quizzesWithQuizSetId);

        List<QuizResponse> quizResponses = toQuizResponse(newQuizzes);

        return RegistQuizSetResponse.from(newQuizSet, quizResponses);
    }

    private static List<QuizResponse> toQuizResponse(List<Quiz> newQuizzes) {

        return newQuizzes.stream().map(QuizResponse::from).toList();

    }

    public List<QuizzesWithQuizSetId> bindQuizSetId(
            Long quizSetId, List<RegistQuizRequest> requestQuizzes) {

        List<QuizzesWithQuizSetId> quizzesWithQuizSetId =
                requestQuizzes.stream()
                        .map(quiz -> QuizzesWithQuizSetId.from(quizSetId, quiz))
                        .toList();

        return quizzesWithQuizSetId;
    }

    @Transactional
    public List<Quiz> createQuizList(List<QuizzesWithQuizSetId> requestQuizzes) {
        if (requestQuizzes.isEmpty()) {
            throw new InvalidRequestException(INVALID_QUIZ_COUNT);
        }

        List<Quiz> quizzes = toQuizEntityList(requestQuizzes);

        return quizRepository.saveAll(quizzes);
    }

    // QuizSet 객체생성
    public QuizSet createQuizSet(RegistQuizSetRequest request) {

        QuizSet quizSet =
                QuizSet.builder()
                        .creatorId(request.getCreatorId())
                        .quizSetName(request.getQuizSetName())
                        .quizSetType(request.getQuizSetType().name())
                        .quizSetRunCnt(0)
                        .build();
        return quizSetRepository.save(quizSet);
    }

    public List<Quiz> toQuizEntityList(List<QuizzesWithQuizSetId> registQuizRequestList) {
        return registQuizRequestList.stream()
                .map(
                        registQuizRequest ->
                                Quiz.builder()
                                        .quizSetId(registQuizRequest.getQuizSetId())
                                        .question(registQuizRequest.getQuestion())
                                        .hint(registQuizRequest.getHint())
                                        .answer(registQuizRequest.getAnswer())
                                        .build())
                .toList();
    }
}
