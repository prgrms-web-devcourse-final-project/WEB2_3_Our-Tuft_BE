package com.example.web2_3_ourtuft_be.quiz.service;

import com.example.web2_3_ourtuft_be.quiz.dto.QuizCategoryResponse;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizCategory;
import com.example.web2_3_ourtuft_be.quiz.repository.QuizCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizCategoryService {

    private QuizCategoryRepository quizCategoryRepository;

    public List<QuizCategoryResponse> getQuizCategories() {

        List<QuizCategory> quizCategories = quizCategoryRepository.findAll();
        return quizCategories.stream().map(QuizCategoryResponse::from).toList();


    }

}
