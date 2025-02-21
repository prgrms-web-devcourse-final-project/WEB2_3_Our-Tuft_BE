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

    @Builder
    public Quiz(Long quizSetId, String question, String hint, String answer) {
        this.quizSetId = quizSetId;
        this.question = question;
        this.hint = hint;
        this.answer = answer;
    }

    public static Quiz to(long quizSetId, String question, String hint, String answer) {
        return Quiz.builder()
                .quizSetId(quizSetId)
                .question(question)
                .hint(hint)
                .answer(answer)
                .build();
    }


}
