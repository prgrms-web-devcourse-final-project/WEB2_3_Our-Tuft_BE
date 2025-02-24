package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseHistoryService purchaseHistoryService;
    private final MemberPointService memberPointService;
    private final ItemService itemService;

    @Transactional
    public void purchase(List<Long> items) {

        Long userId = 1L;

        int totalPrice = calculateTotalPrice(items);
        int totalQuantity = items.size();

        memberPointService.updatePoints(
                userId, totalPrice, PointChangeType.DECREASE, PointChangeReason.PURCHASE);

        purchaseHistoryService.savePurchaseHistory(userId, items, totalPrice, totalQuantity);
    }

    private int calculateTotalPrice(List<Long> items) {
        return items.stream().mapToInt(itemId -> itemService.getItem(itemId).getPrice()).sum();
    }
}
