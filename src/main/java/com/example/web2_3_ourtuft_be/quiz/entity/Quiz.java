package com.example.web2_3_ourtuft_be.quiz.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Quiz extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "quiz_detail_id")
    private QuizDetail quizDetail;

    @Column(name="quiz_name",nullable = false )
    private String quiz;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_type", nullable = false)
    private QuizType quizType;

    @Column(name = "quiz_run_cnt", nullable = false)
    private int quizRunCnt;


}
