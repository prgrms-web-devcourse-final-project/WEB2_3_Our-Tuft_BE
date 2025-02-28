package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.shop.repository.OrderRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Long userId, int totalQuantity) {

        Order orderHistory =
                Order.builder()
                        .userId(userId)
                        .totalQuantity(totalQuantity)
                        .orderAt(LocalDateTime.now())
                        .build();

        return orderRepository.save(orderHistory);
    }
}
