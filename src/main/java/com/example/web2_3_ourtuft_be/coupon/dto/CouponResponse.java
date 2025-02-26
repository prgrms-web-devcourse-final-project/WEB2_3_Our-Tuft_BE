package com.example.web2_3_ourtuft_be.coupon.dto;

import com.example.web2_3_ourtuft_be.coupon.entity.Coupon;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponResponse {
    private Long id;
    private String description;
    private int discountAmount;
    private int stock;
    private int minOrderAmount;
    private LocalDate startDate;
    private LocalDate endDate;

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .description(coupon.getDescription())
                .discountAmount(coupon.getDiscountAmount())
                .stock(coupon.getStock())
                .minOrderAmount(coupon.getMinOrderAmount())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .build();
    }
}
