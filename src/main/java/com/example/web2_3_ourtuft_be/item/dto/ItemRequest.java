package com.example.web2_3_ourtuft_be.item.dto;

import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {
    private String name;
    private Category category;
    private String imageUrl;
    private String nickColor;
    private int price;
    private int stock;
}
