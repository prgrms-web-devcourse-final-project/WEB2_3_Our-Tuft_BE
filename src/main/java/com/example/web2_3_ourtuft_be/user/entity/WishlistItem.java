package com.example.web2_3_ourtuft_be.user.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name= "item_id", nullable = false)
    private Long itemId;

    public WishlistItem(Long userId, Long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }
}
