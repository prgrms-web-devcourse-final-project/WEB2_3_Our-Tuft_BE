package com.example.web2_3_ourtuft_be.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_records")
public class MemberRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private int totalGames = 0;
    private int winCount = 0;

    @Version private int version;

    public void increaseTotalGames() {
        this.totalGames++;
    }

    public void increaseWinCount() {
        this.winCount++;
    }

    public double getWinRate() {
        if (totalGames == 0) {
            return 0.0;
        }
        return Math.round(((double) winCount / totalGames) * 10000) / 100.0;
    }
}
