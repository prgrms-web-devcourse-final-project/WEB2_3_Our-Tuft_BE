package com.example.web2_3_ourtuft_be.quiz.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizType;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class QuizSet extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_set_id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "quizSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quiz> quizzes = new ArrayList<>();

    @Column(name = "quiz_set_name", nullable = false)
    private String quizSetName;

    
    @Column(name = "quiz_set_type", nullable = false)
    private String quizSetType;

    @Column(name = "quiz_set_run_cnt", nullable = false)
    private int quizSetRunCnt;

    public QuizSet(
            List<Quiz> quizzes, String quizSetName, String quizSetType, int quizSetRunCnt) {
        this.quizzes = quizzes;
        this.quizSetName = quizSetName;
        this.quizSetType = quizSetType;
        this.quizSetRunCnt = quizSetRunCnt;
    }

    public static QuizSet of(
            List<Quiz> quizzes, String quizSetName, String quizSetType, int quizSetRunCnt) {

        QuizSet quizSet = new QuizSet(quizzes, quizSetName, quizSetType, quizSetRunCnt);

        // 각 Quiz 객체에 quizSet 바인딩
        for (Quiz quiz : quizzes) {
            quiz.bindQuizSet(quizSet);
        }

        return quizSet;
    }
}
