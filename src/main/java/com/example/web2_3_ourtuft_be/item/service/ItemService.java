package com.example.web2_3_ourtuft_be.item.service;

import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.item.dto.ItemRequest;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import com.example.web2_3_ourtuft_be.item.repository.ItemRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public PageResponse<ItemResponse> getItems(String category, String keyword, Pageable pageable) {
        Slice<Item> items;

        items = itemRepository.findFilteredItems(category, keyword, "default", pageable);

        List<ItemResponse> itemResponses =
                items.stream().map(ItemResponse::from).collect(Collectors.toList());

        return new PageResponse<>(
                itemResponses,
                items.hasNext(),
                items.isFirst(),
                items.isLast(),
                items.isEmpty(),
                items.getNumberOfElements());
    }

    @Transactional
    public ItemResponse registerItem(ItemRequest request) {

        validateValue(request);

        Item item =
                Item.builder()
                        .name(request.getName())
                        .category(request.getCategory().name())
                        .imageUrl(request.getImageUrl())
                        .nickColor(request.getNickColor())
                        .originalPrice(request.getPrice())
                        .build();

        itemRepository.save(item);

        return ItemResponse.from(item);
    }

    @Transactional
    public ItemResponse updateItem(Long itemId, ItemRequest request) {
        Item item =
                itemRepository
                        .findById(itemId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.ITEM));

        validateValue(request);

        item.update(
                request.getName(),
                request.getCategory().name(),
                request.getImageUrl(),
                request.getNickColor(),
                request.getPrice(),
                request.getStock());

        return ItemResponse.from(item);
    }

    private void validateValue(ItemRequest request) {

        Category category = request.getCategory();

        if (Category.NICKNAME == category) {
            if (request.getNickColor() == null) {
                throw new InvalidRequestException(InvalidRequestMessages.INVALID_COLOR_VALUE);
            }
        } else {
            if (request.getImageUrl() == null) {
                throw new InvalidRequestException(InvalidRequestMessages.INVALID_IMAGE_VALUE);
            }
        }
    }

    public void deleteItem(Long itemId) {

        Item item =
                itemRepository
                        .findById(itemId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.ITEM));

        itemRepository.delete(item);
    }

    public Item getItem(Long itemId) {

        return itemRepository
                .findById(itemId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.ITEM));
    }

    public void validItemId(Long itemId) {
        boolean exists = itemRepository.existsById(itemId);
        if (!exists) {
            throw new NotFoundException(NotFoundMessages.ITEM);
        }
    }

    public PageResponse<ItemResponse> getItemInfoByWishlist(List<Long> itemIds, Pageable pageable) {
        // itemIds가 비어있으면 빈 페이지 반환
        if (itemIds == null || itemIds.isEmpty()) {
            return new PageResponse<>(Collections.emptyList(), false, true, true, true, 0);
        }

        // itemIds를 사용해 Item 조회
        Slice<Item> items = itemRepository.findAllByIdIn(itemIds, pageable);

        // Item -> ItemResponse 변환
        List<ItemResponse> itemResponses =
                items.stream().map(ItemResponse::from).collect(Collectors.toList());

        return new PageResponse<>(
                itemResponses,
                items.hasNext(),
                items.isFirst(),
                items.isLast(),
                items.isEmpty(),
                items.getNumberOfElements());
    }

    public List<Item> getItemsByIds(List<Long> itemIds) {
        return itemRepository.findAllById(itemIds);
    }

    public List<Item> getItemsByDiscountId(Long discountId) {
        return itemRepository.findByDiscountId(discountId);
    }

    @Transactional
    public void setDiscountId(List<Long> itemIds, Discount discount) {
        List<Item> items = itemRepository.findAllById(itemIds);
        for (Item item : items) {
            item.updateDiscountId(discount.getId());
        }
    }
}
