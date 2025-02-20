package com.example.web2_3_ourtuft_be.item.dto;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {
    private Long id;
    private String name;
    private String category;
    private String imageUrl;
    private String nickColor;
    private int price;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .imageUrl(item.getImageUrl())
                .nickColor(item.getNickColor())
                .price(item.getPrice())
                .build();
    }
}
