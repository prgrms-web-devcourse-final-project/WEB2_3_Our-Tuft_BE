package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.shop.entity.PurchaseHistory;
import com.example.web2_3_ourtuft_be.shop.entity.PurchaseItem;
import com.example.web2_3_ourtuft_be.shop.repository.PurchaseHistoryRepository;
import com.example.web2_3_ourtuft_be.shop.repository.PurchaseItemRepository;
import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.entity.PointHistory;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.repository.MemberPointRepository;
import com.example.web2_3_ourtuft_be.user.repository.PointHistoryRepository;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberPointService memberPointService;
    private final ItemService itemService;

    @Transactional
    public void purchase(List<Long> items) {

        Long userId = 1L;

        int totalPrice = calculateTotalPrice(items);
        int totalQuantity = items.size();

        processPointUsage(userId, totalPrice);

        PurchaseHistory purchaseHistory = savePurchaseHistory(userId, totalPrice, totalQuantity);

        savePurchaseItems(purchaseHistory.getId(), items);
    }

    private int calculateTotalPrice(List<Long> items) {
        return items.stream().mapToInt(itemId -> itemService.getItem(itemId).getPrice()).sum();
    }

    private void processPointUsage(Long userId, int totalPrice) {

        MemberPoint memberPoint = memberPointService.getPoint(userId);

        memberPointService.updatePoints(userId, -totalPrice);

        PointHistory pointHistory =
                PointHistory.builder()
                        .memberPointId(memberPoint.getId())
                        .pointChange(-totalPrice)
                        .reason(PointChangeReason.PURCHASE.name())
                        .usageTime(LocalDateTime.now())
                        .build();
        pointHistoryRepository.save(pointHistory);
    }

    private PurchaseHistory savePurchaseHistory(Long userId, int totalPrice, int totalQuantity) {
        PurchaseHistory purchaseHistory =
                PurchaseHistory.builder()
                        .userId(userId)
                        .totalPrice(totalPrice)
                        .totalQuantity(totalQuantity)
                        .purchasedAt(LocalDateTime.now())
                        .build();

        return purchaseHistoryRepository.save(purchaseHistory);
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
