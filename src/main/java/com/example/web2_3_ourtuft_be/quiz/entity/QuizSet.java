package com.example.web2_3_ourtuft_be.quiz.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class QuizSet extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizset_id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "quizSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();

    @Column(name="quizset_name",nullable = false )
    private String quiz;

    @Enumerated(EnumType.STRING)
    @Column(name = "quizset_type", nullable = false)
    private QuizType quizsetType;

    @Column(name = "quizset_run_cnt", nullable = false)
    private int quizsetRunCnt;

}
