package com.example.web2_3_ourtuft_be.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderHistoryId;

    private Long itemId;

    // item 가격, 할인적용 등이 변경될 수 있으므로, 구매 시점의 가격 정보가 필요
    private int originalPrice;
    private int discountPrice;
    private int finalPrice;
}
