package com.example.web2_3_ourtuft_be.user.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "memberpoint_id", nullable = false)
    private Long memberPointId;

    @Column(nullable = false)
    private int pointChange;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime usageTime;
}