package com.example.web2_3_ourtuft_be.quiz.service;

import static com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages.INVALID_QUIZ_COUNT;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
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
    public void deleteQuizSet(Long quizSetId) {

        boolean existsByQuizSetId = quizRepository.existsByQuizSetId(quizSetId);

        if (existsByQuizSetId) {
            quizRepository.deleteByQuizSetId(quizSetId);

            quizSetRepository.deleteById(quizSetId);
        } else {
            throw new NotFoundException(NotFoundMessages.NOT_FOUND_QUIZ_SET);
        }
    }

    @Transactional
    public QuizSetResponse registQuizSet(QuizSetRequest request) {

        QuizSet newQuizSet = createQuizSet(request);

        List<QuizRequest> quizListDTO = request.getQuizzes();

        bindQuizSetId(newQuizSet.getId(), quizListDTO);

        List<Quiz> newQuizzes = createQuizList(quizListDTO);

        return toQuizSetResponse(newQuizSet, newQuizzes);
    }

    public void bindQuizSetId(Long quizSetId, List<QuizRequest> quizListDTO) {
        quizListDTO.forEach(quiz -> quiz.setQuizSetId(quizSetId));
    }

    public List<Quiz> createQuizList(List<QuizRequest> quizRequestList) {
        if (quizRequestList.isEmpty()) {
            throw new InvalidRequestException(INVALID_QUIZ_COUNT);
        }
        List<Quiz> quizzes = toQuizEntityList(quizRequestList);
        return quizRepository.saveAll(quizzes);
    }

    // QuizSet 객체생성
    public QuizSet createQuizSet(QuizSetRequest request) {

        QuizSet quizSet =
                QuizSet.builder()
                        .creatorId(request.getCreatorId())
                        .quizSetName(request.getQuizSetName())
                        .quizSetType(request.getQuizSetType().name())
                        .quizSetRunCnt(0)
                        .build();
        return quizSetRepository.save(quizSet);
    }

    public QuizSetResponse toQuizSetResponse(QuizSet newQuizSet, List<Quiz> newQuizzes) {

        List<QuizResponse> quizResponses = newQuizzes.stream().map(QuizResponse::from).toList();

        return QuizSetResponse.from(newQuizSet, quizResponses);
    }

    public List<Quiz> toQuizEntityList(List<QuizRequest> quizRequestList) {
        return quizRequestList.stream().map(QuizRequest::toEntity).toList();
    }
}
