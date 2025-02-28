package com.example.web2_3_ourtuft_be.shop.dto;

import com.example.web2_3_ourtuft_be.shop.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class OrderListResponse {
    private final Long purchaseId;
    private final LocalDateTime purchasedAt;
    private final int quantity;
    private final int totalOriginalPrice;
    private final int totalDiscountPrice;
    private final int totalFinalPrice;
    private final List<OrderItemDto> items;

    public static OrderListResponse from(Order order, List<OrderItemDto> items) {
        return OrderListResponse.builder()
                .purchaseId(order.getId())
                .purchasedAt(order.getPurchasedAt())
                .quantity(order.getTotalQuantity())
                .totalDiscountPrice(order.getTotalDiscountPrice())
                .totalOriginalPrice(order.getTotalOriginalPrice())
                .totalFinalPrice(order.getTotalFinalPrice())
                .items(items)
                .build();
    }
}
