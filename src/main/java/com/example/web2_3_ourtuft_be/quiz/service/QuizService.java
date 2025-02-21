package com.example.web2_3_ourtuft_be.quiz.service;

import com.example.web2_3_ourtuft_be.quiz.QuizRepository;
import com.example.web2_3_ourtuft_be.quiz.QuizSetRepository;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetResponse;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizeSetRequest;
import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
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
    public QuizSetResponse createQuizSet(QuizeSetRequest request) {
        QuizSet quizSet =
                QuizSet.to(request.getQuizSetName(), request.getQuizSetType().name(), 0);
        QuizSet savedQuizSet = quizSetRepository.save(quizSet);

        List<Quiz> quizzes =
                request.getQuizzes().stream()
                        .map(
                                quiz ->
                                        Quiz.to(
                                                savedQuizSet.getId(),
                                                quiz.getQuestion(),
                                                quiz.getHint(),
                                                quiz.getAnswer()))
                        .toList();
        List<Quiz> savedQuiz = quizRepository.saveAll(quizzes);

        List<QuizResponse> savedQuizDTO =
                savedQuiz.stream()
                        .map(
                                quiz ->
                                        QuizResponse.builder()
                                                .question(quiz.getQuestion())
                                                .hint(quiz.getHint())
                                                .answer(quiz.getAnswer())
                                                .build())
                        .toList();

        QuizSetResponse response =
                QuizSetResponse.builder()
                        .quizSetId(savedQuizSet.getId())
                        .quizzes(savedQuizDTO)
                        .quizSetName(savedQuizSet.getQuizSetName())
                        .quizSetType(request.getQuizSetType())
                        .build();

        return response;
    }

    public static QuizeSetRequest createTestData() {
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

        return QuizeSetRequest.builder()
                .quizzes(quizzes)
                .quizSetName(quizSetName)
                .quizSetType(QuizType.OX)
                .build();
    }
}
