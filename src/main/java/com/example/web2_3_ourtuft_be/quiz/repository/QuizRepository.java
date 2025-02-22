package com.example.web2_3_ourtuft_be.quiz.repository;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {}
