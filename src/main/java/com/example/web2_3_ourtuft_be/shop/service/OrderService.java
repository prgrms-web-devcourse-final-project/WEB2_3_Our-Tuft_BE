package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.shop.repository.OrderHistoryRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderHistoryRepository orderHistoryRepository;

    public Order createOrderHistory(Long userId) {

        Order orderHistory =
                Order.builder().userId(userId).purchasedAt(LocalDateTime.now()).build();

        return orderHistoryRepository.save(orderHistory);
    }
}
