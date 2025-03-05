package com.example.web2_3_ourtuft_be.global.initializer;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import com.example.web2_3_ourtuft_be.item.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ItemInitializer implements CommandLineRunner {

    private static final String S3_URL = "https://team09-bucket.s3.ap-northeast-2.amazonaws.com";
    private static final String DEFAULT_PREFIX = "default-";
    private static final String PNG = ".png";
    private final ItemRepository itemRepository;

    public ItemInitializer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 3; i++) {
            Category category = Category.values()[i];
            // 기본 아이템이 없는 경우에만 추가
            if (!itemRepository.existsByName(DEFAULT_PREFIX + category.name())) {
                saveItem(createDefaultItem(category));
            }

            // 해당 카테고리 아이템이 없을 경우에만 추가
            if (itemRepository.countByCategory(category.name()) <= 1) {
                saveItems(createCategoryItems(category));
            }
        }
    }

    private List<Item> createCategoryItems(Category category) {
        return IntStream.range(2, 4)
                .mapToObj(i -> createItem(category, i))
                .collect(Collectors.toList());
    }

    private Item createItem(Category category, int index) {
        return Item.builder()
                .name(category.name() + index)
                .category(category.name())
                .imageUrl(formatImageUrl(category, String.valueOf(index)))
                .originalPrice(300)
                .stock(200)
                .build();
    }

    private Item createDefaultItem(Category category) {
        return Item.builder()
                .name(DEFAULT_PREFIX + category.name())
                .category(category.name())
                .imageUrl(formatImageUrl(category, DEFAULT_PREFIX))
                .originalPrice(300)
                .stock(200)
                .build();
    }

    private String formatImageUrl(Category category, String identifier) {
        return String.format(
                "%s/%s/%s%s%s",
                S3_URL,
                category.name().toLowerCase(),
                identifier,
                category.name().toLowerCase(),
                PNG);
    }

    private void saveItem(Item item) {
        itemRepository.save(item);
    }

    private void saveItems(List<Item> items) {
        itemRepository.saveAll(items);
    }
}
