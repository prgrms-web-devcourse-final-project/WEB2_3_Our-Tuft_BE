package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.WishlistItem;
import com.example.web2_3_ourtuft_be.user.repository.WishlistItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WishlistItemService {

    private final WishlistItemRepository wishlistItemRepository;

    public WishlistItemService(WishlistItemRepository wishlistItemRepository) {
        this.wishlistItemRepository = wishlistItemRepository;
    }

    public void addItem(Long userId, Long itemId) {
        validDuplicateWishItem(userId, itemId);
        wishlistItemRepository.save(new WishlistItem(userId, itemId));
    }

    public void validDuplicateWishItem(Long userId, Long itemId) {
        boolean exists = wishlistItemRepository.existsByUserIdAndItemId(userId, itemId);
        if (exists) {
            throw new DuplicatedException(DuplicatedMessages.WISH_ITEM);
        }
    }

    public void deleteWishItem(Long userId, Long itemId) {
        WishlistItem item =
                wishlistItemRepository
                        .findByUserIdAndItemId(userId, itemId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.WISH_ITEM));

        wishlistItemRepository.delete(item);
    }

    public List<Long> getWishItemIds(Long userId) {
        return wishlistItemRepository.findItemIdByUserId(userId);
    }
}
