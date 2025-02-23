package com.example.web2_3_ourtuft_be.quiz.repository;

import com.example.web2_3_ourtuft_be.quiz.entity.Quiz;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    @Modifying
    @Query("DELETE FROM Quiz q WHERE q.quizSetId = :quizSetId")
    void deleteByQuizSetId(@Param("quizSetId") Long quizSetId);

    Optional<List<Quiz>> findAllByQuizSetId(Long quizSetId);

    boolean existsByQuizSetId(Long quizSetId);
}
