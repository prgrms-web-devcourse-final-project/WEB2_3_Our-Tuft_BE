package com.example.web2_3_ourtuft_be.shop.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.shop.dto.OrderItemDto;
import com.example.web2_3_ourtuft_be.shop.dto.OrderListResponse;
import com.example.web2_3_ourtuft_be.shop.entity.Order;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderFacadeService {
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final MemberPointService memberPointService;

    public OrderFacadeService(
            ItemService itemService,
            OrderService orderService,
            OrderItemService orderItemService,
            MemberPointService memberPointService) {
        this.itemService = itemService;
        this.orderService = orderService;
        this.orderItemService = orderItemService;
        this.memberPointService = memberPointService;
    }

    @Transactional
    public OrderListResponse order(Long userId, List<Long> itemIds) {
        List<Item> items = itemService.getItemsByIds(itemIds);
        validateItemIds(items, itemIds);
        Order newOrder = orderService.createOrder(userId, items.size());
        List<OrderItemDto> orderItemDtoList = orderItemService.createOrderItems(newOrder, items);
        memberPointService.updatePoints(
                userId,
                newOrder.getTotalFinalPrice(),
                PointChangeType.DECREASE,
                PointChangeReason.PURCHASE);

        return OrderListResponse.from(newOrder, orderItemDtoList);
    }

    public void validateItemIds(List<Item> items, List<Long> itemIds) {
        // 요청한 itemIds 중 존재하지 않는 id가 있는지 검증, 검증 로직 분리해야함
        List<Long> foundIds =
                items.stream().map(Item::getId).collect(Collectors.toCollection(ArrayList::new));
        List<Long> notFoundIds =
                itemIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toCollection(ArrayList::new));

        // 존재하지 않는 Id가 있다면 예외 발생 , 어떤 itemId가 잘못된건지 나오면 좋을듯한데...
        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException((NotFoundMessages.ITEM));
        }
    }
}
