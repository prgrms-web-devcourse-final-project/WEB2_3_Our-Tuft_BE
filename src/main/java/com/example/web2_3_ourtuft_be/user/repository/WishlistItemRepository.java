package com.example.web2_3_ourtuft_be.user.repository;

import com.example.web2_3_ourtuft_be.user.entity.WishlistItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    boolean existsByUserIdAndItemId(Long userId, Long itemId);

    Optional<WishlistItem> findByUserIdAndItemId(Long userId, Long itemId);

    List<Long> findItemIdByUserId(Long userId);
}
