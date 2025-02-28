package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.shop.dto.OrderItemDto;
import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.shop.entity.OrderItem;
import com.example.web2_3_ourtuft_be.shop.repository.OrderItemRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private OrderItemRepository orderItemRepository;

    public List<OrderItemDto> createOrderItems(Order orderHistory, List<Item> items) {

        int totalOriginalPrice = 0;
        int totalDiscountPrice = 0;
        int totalFinalPrice = 0;
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();

        for (Item item : items) {

            int originalPrice = item.getOriginalPrice();
            int discountPrice = item.getDiscountPrice();
            int finalPrice = originalPrice - discountPrice;

            OrderItem itemData =
                    OrderItem.builder()
                            .orderHistoryId(orderHistory.getId())
                            .itemId(item.getId())
                            .originalPrice(originalPrice)
                            .discountPrice(discountPrice)
                            .finalPrice(finalPrice)
                            .build();

            OrderItem orderItem = orderItemRepository.save(itemData);

            totalOriginalPrice += originalPrice;
            totalDiscountPrice += discountPrice;
            totalFinalPrice += finalPrice;

            OrderItemDto orderItemDto = OrderItemDto.from(orderItem, item);
            orderItemDtoList.add(orderItemDto);
        }
        orderHistory.recordPrice(totalOriginalPrice, totalDiscountPrice, totalFinalPrice);
        return orderItemDtoList;
    }
}
