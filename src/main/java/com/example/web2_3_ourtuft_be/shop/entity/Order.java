package com.example.web2_3_ourtuft_be.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int totalOriginalPrice;
    private int totalDiscountPrice;
    private int totalFinalPrice;
    private int totalQuantity;
    private LocalDateTime purchasedAt;

    public void recordPrice(int totalOriginalPrice, int totalDiscountPrice, int totalFinalPrice) {
        this.totalOriginalPrice = totalOriginalPrice;
        this.totalDiscountPrice = totalDiscountPrice;
        this.totalFinalPrice = totalFinalPrice;
    }
}
