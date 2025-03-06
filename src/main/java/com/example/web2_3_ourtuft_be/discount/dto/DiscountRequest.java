package com.example.web2_3_ourtuft_be.discount.dto;

import com.example.web2_3_ourtuft_be.discount.entity.enums.DiscountType;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiscountRequest {
    private DiscountType type;
    private int value;
    private LocalDate startDate;
    private LocalDate endDate;
}
