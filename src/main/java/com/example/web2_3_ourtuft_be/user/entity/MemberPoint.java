package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
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

//    private int points = 0;
    @Embedded
    private Point point;

    public MemberPoint(Long userId) {
        this.userId = userId;
        point = new Point(0);
    }

    public int getPoints() {
        return point.getValue();
    }

    public void updatePoints(PointChangeType type, int value) {
        this.point = point.updatePoint(value, type);
    }
}
