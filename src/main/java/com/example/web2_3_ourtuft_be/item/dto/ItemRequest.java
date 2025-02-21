package com.example.web2_3_ourtuft_be.item.dto;

import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import lombok.Getter;

@Getter
public class ItemRequest {
    private String name;
    private Category category;
    private String imageUrl;
    private String nickColor;
    private int price;
    private int stock;
}
