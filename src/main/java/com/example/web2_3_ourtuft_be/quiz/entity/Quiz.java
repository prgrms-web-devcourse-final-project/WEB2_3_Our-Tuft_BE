package com.example.web2_3_ourtuft_be.quiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id", nullable = false)
    private Long id;

    private Long quizSetId;
    private String question;
    private String hint;
    private String answer;

    public static Quiz create(long quizSetId, String question, String hint, String answer) {
        Quiz quiz = new Quiz();
        quiz.quizSetId = quizSetId;
        quiz.question = question;
        quiz.hint = hint;
        quiz.answer = answer;
        return quiz;
    }
}
