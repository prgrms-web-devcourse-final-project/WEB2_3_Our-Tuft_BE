package com.example.web2_3_ourtuft_be.quiz.service;

import com.example.web2_3_ourtuft_be.quiz.dto.QuizRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizRepository;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizSetRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QuizSetRepository quizSetRepository;
    private final QuizRepository quizRepository;

    @Transactional
    public QuizSetResponse registQuizSet(QuizSetRequest request) {

        QuizSet newQuizSet = insertQuizSet(request);

        List<Quiz> newQuiz = insertQuizList(request.getQuizzes());

        List<QuizResponse> newQuizDTO = toQuizResponse(newQuiz);

        QuizSetResponse response = toQuizSetResponse(request, newQuizSet, newQuizDTO);

        return response;
    }

    public List<Quiz> insertQuizList(List<QuizRequest> request) {
        List<Quiz> quizzes = toQuizEntityList(request);
        return quizRepository.saveAll(quizzes);
    }

    public QuizSet insertQuizSet(QuizSetRequest request) {
        QuizSet quizSet =
                QuizSet.builder()
                        .quizSetName(request.getQuizSetName())
                        .quizSetType(request.getQuizSetType().name())
                        .quizSetRunCnt(0)
                        .build();
        return quizSetRepository.save(quizSet);
    }

    public QuizSetResponse toQuizSetResponse(
            QuizSetRequest request, QuizSet newQuizSet, List<QuizResponse> newQuizDTO) {
        QuizSetResponse quizSetResponse =
                QuizSetResponse.builder()
                        .quizSetId(newQuizSet.getId())
                        .quizzes(newQuizDTO)
                        .quizSetName(newQuizSet.getQuizSetName())
                        .quizSetType(newQuizSet.getQuizSetType())
                        .build();
        return quizSetResponse;
    }

    public List<QuizResponse> toQuizResponse(List<Quiz> newQuiz) {
        return newQuiz.stream().map(QuizResponse::to).toList();
    }

    public List<Quiz> toQuizEntityList(List<QuizRequest> request) {
        return request.stream().map(QuizRequest::to).toList();
    }

    // TODO : 테스트를 위한 메서드로 추후 삭제 예정 요청 데이터 랜덤생성 메서드
    //  요청 데이터 랜덤생성 메서드
    public static QuizSetRequest createTestData() {
        Random random = new Random();
        int randomInt = random.nextInt(100);
        List<QuizRequest> quizzes = new ArrayList<>();
        String quizSetName = "문제" + randomInt;
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

        return QuizSetRequest.builder()
                .quizzes(quizzes)
                .quizSetName(quizSetName)
                .quizSetType(QuizType.OX)
                .build();
    }
}
