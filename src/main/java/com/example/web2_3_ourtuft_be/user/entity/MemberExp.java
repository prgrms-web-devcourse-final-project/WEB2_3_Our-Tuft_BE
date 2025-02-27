package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "member_levels")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberExp extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "level_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private int exp = 0;

    public MemberExp(Long userId) {
        this.userId = userId;
    }

    public void increaseExp(int exp) {
        this.exp += exp;
    }

    public int getLevel() {
        return exp / 100;
    }

    public int getProgress() {
        return exp % 100;
    }
    // 일단 각 레벨별 필요 경험치는 100으로 설정
}
