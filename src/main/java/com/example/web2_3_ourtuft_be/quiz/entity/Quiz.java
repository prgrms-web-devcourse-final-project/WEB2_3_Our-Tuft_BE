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

    @Column(name = "quiz_set_id", nullable = false)
    private Long quizSetId;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "hint", nullable = false)
    private String hint;

    @Column(name = "answer", nullable = false)
    private String answer;

    @Builder
    public Quiz(Long quizSetId, String question, String hint, String answer) {
        this.quizSetId = quizSetId;
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }

    public void updateQuestion(String question) {
        this.question = question;
    }

    public void updateAnswer(String answer) {
        this.answer = answer;
    }

    public void updateHint(String hint) {
        this.hint = hint;
    }
}
