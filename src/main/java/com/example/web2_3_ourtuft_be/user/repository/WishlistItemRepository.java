package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.WishlistItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    Optional<WishlistItem> findByUserIdAndItemId(Long userId, Long itemId);

    @Query("SELECT w.itemId FROM WishlistItem w WHERE w.userId = :userId")
    List<Long> findItemIdByUserId(Long userId);
}
