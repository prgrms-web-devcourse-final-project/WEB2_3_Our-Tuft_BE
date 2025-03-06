package com.example.web2_3_ourtuft_be.item.service;

import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import com.example.web2_3_ourtuft_be.discount.entity.enums.DiscountType;
import com.example.web2_3_ourtuft_be.discount.service.DiscountService;
import com.example.web2_3_ourtuft_be.item.entity.Item;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemDiscountService {

    private final ItemService itemService;
    private final DiscountService discountService;

    @Transactional
    public void updateDiscountPrice(LocalDate today) {

        List<Discount> discountsToApply = discountService.getDiscountByStartDate(today);
        for (Discount discount : discountsToApply) {
            applyDiscount(discount);
        }

        List<Discount> discountsToExpire = discountService.getDiscountByEndDate(today.minusDays(1));
        for (Discount discount : discountsToExpire) {
            removeDiscount(discount);
        }
    }

    private void applyDiscount(Discount discount) {
        List<Item> items = itemService.getItemsByDiscountId(discount.getId());

        for (Item item : items) {
            int discountPrice = 0;

            if (discount.getType().equals(DiscountType.PERCENTAGE.name())) {
                discountPrice = (int) (item.getOriginalPrice() * (discount.getValue() / 100.0));
            } else {
                discountPrice = discount.getValue();
            }
            item.updateDiscountPrice(discountPrice);
        }
    }

    private void removeDiscount(Discount discount) {
        List<Item> items = itemService.getItemsByDiscountId(discount.getId());

        for (Item item : items) {
            item.updateDiscountPrice(0);
        }
    }
}
