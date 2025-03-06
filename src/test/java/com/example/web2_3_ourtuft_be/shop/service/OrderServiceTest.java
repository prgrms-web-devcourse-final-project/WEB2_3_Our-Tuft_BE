package com.example.web2_3_ourtuft_be.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.shop.repository.OrderRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;

    @InjectMocks private OrderService orderService;

    @DisplayName("주문 생성 테스트")
    @Test
    void testCreateOrder() {
        // given
        Order order =
                Order.builder().userId(1L).totalQuantity(10).orderAt(LocalDateTime.now()).build();

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // when
        Order response = orderService.createOrder(1L, 10);

        // then
        assertNotNull(response);
        assertEquals(order.getUserId(), response.getUserId());
        assertEquals(order.getTotalQuantity(), response.getTotalQuantity());
        assertEquals(order.getOrderAt().toLocalDate(), response.getOrderAt().toLocalDate());

        verify(orderRepository).save(any(Order.class));
    }
}
