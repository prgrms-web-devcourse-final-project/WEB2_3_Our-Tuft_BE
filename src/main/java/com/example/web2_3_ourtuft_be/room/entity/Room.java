package com.example.web2_3_ourtuft_be.room.entity;

import com.example.web2_3_ourtuft_be.item.game.entity.Game;
import com.example.web2_3_ourtuft_be.quiz.entity.QuizSet;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game gameId;
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private QuizSet quizId;
    @Column(nullable = false)
    private String roomName;

    private boolean disclosure;
    private String roomPassword;
    private int peopleEntering;
    private int round;
    private String host;
    private String gameStatus;
}