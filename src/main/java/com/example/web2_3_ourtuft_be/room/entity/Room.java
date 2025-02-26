package com.example.web2_3_ourtuft_be.room.entity;

import com.example.web2_3_ourtuft_be.quiz.entity.enums.QuizSetType;
import jakarta.persistence.*;
import java.util.Date;
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

    @Column(nullable = false)
    private String roomName;

    private boolean disclosure;
    private String roomPassword;
    private int round;
    private String host;
    private QuizSetType gameType;
    private Date createdAt;
    private Date updatedAt;
}
