package com.example.web2_3_ourtuft_be.user.entity;

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

    @Embedded private Point point;

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

// 포인트가 바뀔때 값을 바꾸는 것이 아니라 포인트를 새로 생성해서 할당
// MemberPoint 자체를 새로 생성해서 업데이트
// public MemberPoint update(PointChangeType type, int value) {
//    Point point = this.point.updatePoint(value, type);
//    return new MemberPoint(this.id, this.userId, point)
// }
