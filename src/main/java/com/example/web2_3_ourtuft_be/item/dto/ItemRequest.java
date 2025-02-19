package com.example.web2_3_ourtuft_be.item.dto;

import lombok.Getter;

@Getter
public class ItemRequest {
    private String name;
    private String category;
    private String description;
    private int price;
    private String appearanceType;
    private String color;
    private String imageUrl;
}
