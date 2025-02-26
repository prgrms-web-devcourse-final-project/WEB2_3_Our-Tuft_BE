package com.example.web2_3_ourtuft_be.coupon.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponRequest {
    private String description;
    private int discountAmount;
    private int stock;
    private int minOrderAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
