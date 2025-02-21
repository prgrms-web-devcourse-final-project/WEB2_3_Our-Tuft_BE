package com.example.web2_3_ourtuft_be.item.dto;

import lombok.Getter;

@Getter
public class ItemRequest {
    private String name;
    private String category;
    private String imageUrl;
    private String nickColor;
    private int price;
    private int stock;
}
