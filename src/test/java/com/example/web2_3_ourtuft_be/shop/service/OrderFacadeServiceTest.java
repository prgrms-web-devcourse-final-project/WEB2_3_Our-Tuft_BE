package com.example.web2_3_ourtuft_be.shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.shop.dto.OrderItemDto;
import com.example.web2_3_ourtuft_be.shop.dto.OrderListResponse;
import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.user.service.InventoryService;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class OrderFacadeServiceTest {

    @Mock private ItemService itemService;

    @Mock private OrderService orderService;

    @Mock private OrderItemService orderItemService;

    @Mock private MemberPointService memberPointService;

    @Mock private InventoryService inventoryService;

    @InjectMocks private OrderFacadeService orderFacadeService;

    private List<Item> mockedItems;
    private Order newOrder;
    List<OrderItemDto> orderItemDtoList;

    @BeforeEach
    void setUp() {

        Item item1 =
                Item.builder()
                        .id(1L)
                        .name("item1")
                        .originalPrice(1000)
                        .discountPrice(100)
                        .discountId(1L)
                        .build();

        Item item2 =
                Item.builder()
                        .id(2L)
                        .name("item2")
                        .originalPrice(1200)
                        .discountPrice(200)
                        .discountId(2L)
                        .build();

        mockedItems = Arrays.asList(item1, item2);

        newOrder =
                Order.builder()
                        .id(1L)
                        .totalOriginalPrice(2200)
                        .totalDiscountPrice(300)
                        .totalFinalPrice(1900)
                        .totalQuantity(2)
                        .orderAt(LocalDateTime.now())
                        .build();

        OrderItemDto orderItemDto1 = new OrderItemDto(1L, "item1", 1000, 100, 900);
        OrderItemDto orderItemDto2 = new OrderItemDto(2L, "item2", 1200, 200, 1000);

        orderItemDtoList = Arrays.asList(orderItemDto1, orderItemDto2);
    }

    @DisplayName("아이템 구매 시 할인이 정상적으로 적용")
    @Test
    void testOrder() {
        // given
        Long userId = 1L;
        List<Long> itemIds = Arrays.asList(1L, 2L);

        when(itemService.getItemsByIds(anyList())).thenReturn(mockedItems);
        when(orderService.createOrder(anyLong(), anyInt())).thenReturn(newOrder);
        when(orderItemService.createOrderItems(any(), anyList())).thenReturn(orderItemDtoList);
        doNothing().when(memberPointService).updatePoints(anyLong(), anyInt(), any(), any());

        // when
        OrderListResponse response = orderFacadeService.order(userId, itemIds);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals(2, response.getQuantity());
        assertEquals(2200, response.getTotalOriginalPrice());
        assertEquals(300, response.getTotalDiscountPrice());
        assertEquals(1900, response.getTotalFinalPrice());
        assertEquals(2, response.getItems().size());
        assertEquals("item1", response.getItems().get(0).getName());
        assertEquals(900, response.getItems().get(0).getFinalPrice());

        verify(inventoryService).createInventory(eq(userId), eq(mockedItems));
    }
}
