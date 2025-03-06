package com.example.web2_3_ourtuft_be.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.shop.dto.OrderItemDto;
import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.shop.entity.OrderItem;
import com.example.web2_3_ourtuft_be.shop.repository.OrderItemRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {

    @Mock private OrderItemRepository orderItemRepository;

    @InjectMocks private OrderItemService orderItemService;

    private Item item1, item2;
    private Order order;

    @BeforeEach
    void setUp() {

        order = mock(Order.class);

        item1 = Item.builder().id(1L).originalPrice(1000).discountPrice(200).build();

        item2 = Item.builder().id(2L).originalPrice(2000).discountPrice(400).build();
    }

    @Test
    @DisplayName("주문한 아이템에 대한 정보가 알맞게 생성")
    void testCreateOrderItems() {

        // given
        List<Item> items = List.of(item1, item2);

        when(order.getId()).thenReturn(1L);

        when(orderItemRepository.save(any(OrderItem.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        List<OrderItemDto> result = orderItemService.createOrderItems(order, items);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals(800, result.get(0).getFinalPrice());
        assertEquals(1600, result.get(1).getFinalPrice());

        verify(order, times(1)).recordPrice(3000, 600, 2400);
    }
}
