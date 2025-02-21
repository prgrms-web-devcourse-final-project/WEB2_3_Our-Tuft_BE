package com.example.web2_3_ourtuft_be.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private int points;

    public void addPoints(int amount) {
        this.points += amount;
    }

    public void subtractPoints(int amount) {
        this.points -= amount;
    }
}
