package com.example.web2_3_ourtuft_be.item.entity;

import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String imageUrl;
    private String nickColor;

    private int price;
    private int stock;

    public void update(
            String name, String category, String imageUrl, String nickColor, int price, int stock) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;

        if (Category.NICKNAME.name().equals(category)) {
            this.nickColor = nickColor;
            this.imageUrl = null;
        } else {
            this.imageUrl = imageUrl;
            this.nickColor = null;
        }
    }
}
