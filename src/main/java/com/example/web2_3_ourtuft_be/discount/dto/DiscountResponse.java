package com.example.web2_3_ourtuft_be.discount.dto;

import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountResponse {

    private Long id;
    private String type;
    private int value;
    private LocalDate startDate;
    private LocalDate endDate;

    public static DiscountResponse from(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .type(discount.getType())
                .value(discount.getValue())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate())
                .build();
    }
}
