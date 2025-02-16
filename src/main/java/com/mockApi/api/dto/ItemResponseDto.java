package com.mockApi.api.dto;

import lombok.Getter;

@Getter
public class ItemResponseDto {
    private final int itemId;
    private final String itemName;
    private final int itemPrice;
    private final String itemCategory;
    private final String itemDescription;
    private final int quantity;
    private final boolean liked;

    public ItemResponseDto(int itemId, String itemName, int itemPrice, String itemCategory, String itemDescription, int quantity, boolean liked) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemCategory = itemCategory;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.liked = liked;
    }
}
