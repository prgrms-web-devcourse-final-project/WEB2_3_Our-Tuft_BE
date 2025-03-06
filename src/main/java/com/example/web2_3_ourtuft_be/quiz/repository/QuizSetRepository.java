package com.example.web2_3_ourtuft_be.quiz.repository;

import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizSetRepository extends JpaRepository<QuizSet, Long> {

    Optional<List<QuizSet>> findAllByQuizSetType(String quizSetType);
}
