package com.example.web2_3_ourtuft_be.shop.dto;

import com.example.web2_3_ourtuft_be.shop.entity.Order;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class OrderListResponse {
    private final Long orderId;
    private final LocalDateTime orderAt;
    private final int quantity;
    private final int totalOriginalPrice;
    private final int totalDiscountPrice;
    private final int totalFinalPrice;
    private final List<OrderItemDto> items;

    public static OrderListResponse from(Order order, List<OrderItemDto> items) {
        return OrderListResponse.builder()
                .orderId(order.getId())
                .orderAt(order.getOrderAt())
                .quantity(order.getTotalQuantity())
                .totalDiscountPrice(order.getTotalDiscountPrice())
                .totalOriginalPrice(order.getTotalOriginalPrice())
                .totalFinalPrice(order.getTotalFinalPrice())
                .items(items)
                .build();
    }
}
