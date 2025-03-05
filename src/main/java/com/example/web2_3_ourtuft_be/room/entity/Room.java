package com.example.web2_3_ourtuft_be.room.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Room extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String roomName;

    private boolean disclosure;
    private String roomPassword;
    private int round;
    private Long hostId;
    private QuizSetType gameType;
    private int maxUsers;
    private int time;

    private boolean isGameRunning;
    private int currentRound;

    public void changeHost(Long newHostId) {
        this.hostId = newHostId;
    }

    public void startGame() {
        isGameRunning = true;
        currentRound = 0;
    }

    public void endGame() {
        isGameRunning = false;
        currentRound = 0;
    }

    public void nextRound() {
        currentRound++;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }
}
