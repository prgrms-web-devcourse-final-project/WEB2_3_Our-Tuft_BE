package com.example.web2_3_ourtuft_be.quiz.service;

import com.example.web2_3_ourtuft_be.quiz.QuizSetRepository;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizRequest;
import com.example.web2_3_ourtuft_be.quiz.dto.QuizSetRequest;
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

    @Transactional
    public QuizSet createQuizSet(QuizSetRequest dto) {
        List<Quiz> quizzes =
                dto.getQuizzes().stream()
                        .map(q -> Quiz.of(q.getQuestion(), q.getHint(), q.getAnswer()))
                        .toList();

        QuizSet quizSet =
                QuizSet.of(quizzes, dto.getQuizSetName(), dto.getQuizSetType().toString(), 0);

        return quizSetRepository.save(quizSet);
    }

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
