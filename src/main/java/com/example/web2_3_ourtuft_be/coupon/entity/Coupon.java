package com.example.web2_3_ourtuft_be.coupon.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private int discountAmount;
    private int stock;
    private int minOrderAmount;

    private LocalDate startDate;
    private LocalDate endDate;

    public void update(
            String description,
            int discountAmount,
            int stock,
            int minOrderAmount,
            LocalDate startDate,
            LocalDate endDate) {
        this.description = description;
        this.discountAmount = discountAmount;
        this.stock = stock;
        this.minOrderAmount = minOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
