package com.example.web2_3_ourtuft_be.item.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.item.dto.ItemRequest;
import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.entity.ItemAppearance;
import com.example.web2_3_ourtuft_be.item.entity.enums.ItemAppearanceType;
import com.example.web2_3_ourtuft_be.item.repository.ItemAppearanceRepository;
import com.example.web2_3_ourtuft_be.item.repository.ItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemAppearanceRepository itemAppearanceRepository;

    @Transactional
    public void registerItem(ItemRequest request) {

        Item item =
                Item.builder()
                        .name(request.getName())
                        .category(request.getCategory())
                        .description(request.getDescription())
                        .price(request.getPrice())
                        .build();

        itemRepository.save(item);

        String appearanceValue = validateAppearanceValue(request);

        ItemAppearance itemAppearance =
                ItemAppearance.builder()
                        .item(item)
                        .type(request.getAppearanceType())
                        .value(appearanceValue)
                        .build();

        itemAppearanceRepository.save(itemAppearance);
    }

    private String validateAppearanceValue(ItemRequest request) {

        ItemAppearanceType appearanceType = ItemAppearanceType.valueOf(request.getAppearanceType());
        String appearanceValue = null;

        if (ItemAppearanceType.IMAGE == appearanceType) {
            if (request.getImageUrl() == null) {
                throw new InvalidRequestException(InvalidRequestMessages.INVALID_IMAGE_VALUE);
            }
            appearanceValue = request.getImageUrl();
        } else if (ItemAppearanceType.COLOR == appearanceType) {
            if (request.getColor() == null) {
                throw new InvalidRequestException(InvalidRequestMessages.INVALID_COLOR_VALUE);
            }
            appearanceValue = request.getColor();
        }

        return appearanceValue;
    }
}
