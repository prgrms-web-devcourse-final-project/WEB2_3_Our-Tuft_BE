package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.user.dto.InventoryItemDto;
import com.example.web2_3_ourtuft_be.user.dto.ItemImageUrlDto;
import com.example.web2_3_ourtuft_be.user.dto.NickNameColorItemDto;
import com.example.web2_3_ourtuft_be.user.entity.Inventory;
import com.example.web2_3_ourtuft_be.user.repository.InventoryRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ItemService itemService;
    private final InventoryRepository inventoryRepository;

    public InventoryItemDto getMyItems(Long userId) {

        List<Inventory> inventories = inventoryRepository.findByUserId(userId);

        List<ItemImageUrlDto> eyeItems = new ArrayList<>();
        List<ItemImageUrlDto> mouthItems = new ArrayList<>();
        List<ItemImageUrlDto> skinItems = new ArrayList<>();
        List<NickNameColorItemDto> nickColorItems = new ArrayList<>();

        for (Inventory inventory : inventories) {

            Category category = Category.valueOf(inventory.getCategory());
            Item item = itemService.getItem(inventory.getItemId());

            if (Category.EYE == category) {
                eyeItems.add(new ItemImageUrlDto(inventory.getItemId(), item.getImageUrl()));
            } else if (Category.MOUTH == category) {
                mouthItems.add(new ItemImageUrlDto(inventory.getItemId(), item.getImageUrl()));
            } else if (Category.SKIN == category) {
                skinItems.add(new ItemImageUrlDto(inventory.getItemId(), item.getImageUrl()));
            } else if (Category.NICKNAME == category) {
                nickColorItems.add(
                        new NickNameColorItemDto(inventory.getItemId(), item.getNickColor()));
            }
        }

        return new InventoryItemDto(eyeItems, mouthItems, skinItems, nickColorItems);
    }

    public void registerDefaultItem(Long userId) {
        List<Inventory> defaultItems =
                List.of(
                        Inventory.create(userId, 1L, Category.EYE),
                        Inventory.create(userId, 4L, Category.MOUTH),
                        Inventory.create(userId, 7L, Category.SKIN),
                        Inventory.create(userId, 10L, Category.NICKNAME));

        inventoryRepository.saveAll(defaultItems);
    }

    public void createInventory(Long userId, List<Item> items) {

        for (Item item : items) {
            Inventory inventory =
                    Inventory.builder()
                            .userId(userId)
                            .itemId(item.getId())
                            .category(Category.valueOf(item.getCategory()))
                            .build();

            inventoryRepository.save(inventory);
        }
    }
}
