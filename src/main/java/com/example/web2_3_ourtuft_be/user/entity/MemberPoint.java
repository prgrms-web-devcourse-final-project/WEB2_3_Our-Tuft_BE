package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
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

    public void updatePoints(int points) {
        if (points < 0) {
            throw new InvalidRequestException(InvalidRequestMessages.INSUFFICIENT_POINTS);
        }
        this.points = points;
    }

}
