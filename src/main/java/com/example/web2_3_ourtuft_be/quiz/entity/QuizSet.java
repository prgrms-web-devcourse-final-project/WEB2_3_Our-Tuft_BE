package com.example.web2_3_ourtuft_be.quiz.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizSet extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_set_id", nullable = false)
    private Long id;

    @Column(name = "quiz_set_name", nullable = false)
    private String quizSetName;

    @Column(name = "quiz_set_type", nullable = false)
    private String quizSetType;

    @Column(name = "quiz_set_run_cnt", nullable = false)
    private int quizSetRunCnt;

    @Builder
    public QuizSet(String quizSetName, String quizSetType, int quizSetRunCnt) {
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
        this.quizSetRunCnt = quizSetRunCnt;
    }
}
