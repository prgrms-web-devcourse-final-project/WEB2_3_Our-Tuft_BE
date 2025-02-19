package com.example.web2_3_ourtuft_be.quiz.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class QuizDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_detail_id", nullable = false)
    private Long id;

    private String question;
    private String hint;
    private String answer;



}
