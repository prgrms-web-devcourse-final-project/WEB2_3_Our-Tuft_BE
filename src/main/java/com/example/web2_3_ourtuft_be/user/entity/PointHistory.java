package com.example.web2_3_ourtuft_be.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_point_id", nullable = false)
    private Long memberPointId;

    private int pointChange;

    private String type;

    private String reason;

    private LocalDateTime usageTime;
}
