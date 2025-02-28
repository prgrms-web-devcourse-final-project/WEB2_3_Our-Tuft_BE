package com.example.web2_3_ourtuft_be.shop.dto;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.shop.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderItemDto {
    private Long itemId;
    private String name;

    private int originalPrice;
    private int discountPrice;
    private int finalPrice;

    public static OrderItemDto from(OrderItem orderItem, Item item) {
        return OrderItemDto.builder()
                .itemId(item.getId())
                .name(item.getName())
                .originalPrice(orderItem.getOriginalPrice())
                .discountPrice(orderItem.getDiscountPrice())
                .finalPrice(orderItem.getFinalPrice())
                .build();
    }
}
