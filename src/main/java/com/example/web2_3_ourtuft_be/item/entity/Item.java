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

    private int originalPrice;
    private int discountPrice;
    private int stock;

    private Long discountId;

    public void update(
            String name,
            String category,
            String imageUrl,
            String nickColor,
            int originalPrice,
            int stock) {
        this.name = name;
        this.category = category;
        this.originalPrice = originalPrice;
        this.stock = stock;

        if (Category.NICKNAME.name().equals(category)) {
            this.nickColor = nickColor;
            this.imageUrl = null;
        } else {
            this.imageUrl = imageUrl;
            this.nickColor = null;
        }
    }

    public void updateDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void updateDiscountId(Long discountId) {
        this.discountId = discountId;
    }
}
