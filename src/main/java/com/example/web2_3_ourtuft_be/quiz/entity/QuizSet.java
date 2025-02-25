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

    @Column(name = "creator_id", nullable = false)
    private String creatorId;

    @Column(name = "quiz_set_name", nullable = false)
    private String quizSetName;

    @Column(name = "quiz_set_category_type", nullable = false)
    private String quizSetCategoryType;

    @Column(name = "quiz_set_type", nullable = false)
    private String quizSetType;

    @Column(name = "quiz_set_run_cnt", nullable = false)
    private int quizSetRunCnt;

    @Builder
    public QuizSet(
            String creatorId,
            String quizSetName,
            String quizSetCategoryType,
            String quizSetType,
            int quizSetRunCnt) {
        this.creatorId = creatorId;
        this.quizSetName = quizSetName;
        this.quizSetCategoryType = quizSetCategoryType;
        this.quizSetType = quizSetType;
        this.quizSetRunCnt = quizSetRunCnt;
    }
}
