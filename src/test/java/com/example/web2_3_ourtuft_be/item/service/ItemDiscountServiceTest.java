package com.example.web2_3_ourtuft_be.item.service;

import static org.mockito.Mockito.*;

import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import com.example.web2_3_ourtuft_be.discount.entity.enums.DiscountType;
import com.example.web2_3_ourtuft_be.discount.service.DiscountService;
import com.example.web2_3_ourtuft_be.item.entity.Item;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemDiscountServiceTest {

    @Mock private ItemService itemService;

    @Mock private DiscountService discountService;

    @InjectMocks private ItemDiscountService itemDiscountService;

    @Test
    @DisplayName("오늘 할인이 끝나면 할인 해제, 할인 시작되면 할인 적용")
    void testUpdateDiscountPrice() {
        // given
        LocalDate today = LocalDate.now();

        Discount percentageDiscount =
                Discount.builder()
                        .id(1L)
                        .type(DiscountType.PERCENTAGE.name())
                        .value(10)
                        .startDate(today)
                        .endDate(today.plusDays(1))
                        .build();

        Discount amountDiscount =
                Discount.builder()
                        .id(2L)
                        .type(DiscountType.AMOUNT.name())
                        .value(500)
                        .startDate(today)
                        .endDate(today.plusDays(1))
                        .build();

        Discount expiredDiscount =
                Discount.builder()
                        .id(3L)
                        .type(DiscountType.PERCENTAGE.name())
                        .value(20)
                        .startDate(today.minusDays(2))
                        .endDate(today.minusDays(1))
                        .build();

        Item item1 = spy(Item.builder().id(1L).originalPrice(1000).discountPrice(0).build());
        Item item2 = spy(Item.builder().id(2L).originalPrice(2000).discountPrice(0).build());
        Item item3 = spy(Item.builder().id(3L).originalPrice(1000).discountPrice(200).build());

        when(discountService.getDiscountByStartDate(today))
                .thenReturn(List.of(percentageDiscount, amountDiscount));
        when(discountService.getDiscountByEndDate(today)).thenReturn(List.of(expiredDiscount));

        when(itemService.getItemsByDiscountId(1L)).thenReturn(List.of(item1));
        when(itemService.getItemsByDiscountId(2L)).thenReturn(List.of(item2));
        when(itemService.getItemsByDiscountId(3L)).thenReturn(List.of(item3));

        // when
        itemDiscountService.updateDiscountPrice(today);

        // then
        verify(item1).updateDiscountPrice(100);
        verify(item2).updateDiscountPrice(500);
        verify(item3).updateDiscountPrice(0);
    }
}
