package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.shop.entity.PurchaseHistory;
import com.example.web2_3_ourtuft_be.shop.entity.PurchaseItem;
import com.example.web2_3_ourtuft_be.shop.repository.PurchaseHistoryRepository;
import com.example.web2_3_ourtuft_be.shop.repository.PurchaseItemRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    public void savePurchaseHistory(
            Long userId, List<Long> items, int totalPrice, int totalQuantity) {
        PurchaseHistory purchaseHistory =
                PurchaseHistory.builder()
                        .userId(userId)
                        .totalPrice(totalPrice)
                        .totalQuantity(totalQuantity)
                        .purchasedAt(LocalDateTime.now())
                        .build();

        purchaseHistoryRepository.save(purchaseHistory);

        savePurchaseItems(purchaseHistory.getId(), items);
    }

    private void savePurchaseItems(Long purchaseHistoryId, List<Long> items) {
        for (Long itemId : items) {
            PurchaseItem purchaseItem =
                    PurchaseItem.builder()
                            .purchaseHistoryId(purchaseHistoryId)
                            .itemId(itemId)
                            .build();
            purchaseItemRepository.save(purchaseItem);
        }
    }
}
