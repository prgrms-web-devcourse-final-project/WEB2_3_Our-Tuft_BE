package com.example.web2_3_ourtuft_be.global.initializer;

import com.example.web2_3_ourtuft_be.item.entity.Item;
import com.example.web2_3_ourtuft_be.item.entity.enums.Category;
import com.example.web2_3_ourtuft_be.item.repository.ItemRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ItemInitializer implements CommandLineRunner {

    private static final String S3_URL = "https://team09-bucket.s3.ap-northeast-2.amazonaws.com";
    private static final String PNG = ".png";
    private final ItemRepository itemRepository;

    public ItemInitializer(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 3; i++) {
            Category category = Category.values()[i];

            if (itemRepository.countByCategory(category.name()) < 1) {
                saveItems(createCategoryItems(category));
            }
        }

        if (itemRepository.countByCategory(Category.NICKNAME.name()) < 1) {
            createNickItem();
        }
    }

    private void createNickItem() {

        List<Map.Entry<String, String>> colorNames =
                List.of(
                        Map.entry("#000000", "Black"),
                        Map.entry("#B91C1C", "Red"),
                        Map.entry("#166534", "Green"),
                        Map.entry("#7E22CE", "Purple"),
                        Map.entry("#4338CA", "Blue"),
                        Map.entry("#ED4D0E", "Orange"),
                        Map.entry("#F761A0", "Pink"),
                        Map.entry("#DDDD05", "Yellow"));

        for (Map.Entry<String, String> entry : colorNames) {
            Item item =
                    Item.builder()
                            .name(entry.getValue())
                            .category(Category.NICKNAME.name())
                            .nickColor(entry.getKey())
                            .originalPrice(300)
                            .stock(200)
                            .build();
            itemRepository.save(item);
        }
    }

    private List<Item> createCategoryItems(Category category) {
        return IntStream.range(1, 11)
                .mapToObj(i -> createItem(category, i))
                .collect(Collectors.toList());
    }

    private Item createItem(Category category, int index) {
        String itemName = (index == 1 ? "default-" : "") + category.name() + index;
        return Item.builder()
                .name(itemName)
                .category(category.name())
                .imageUrl(formatImageUrl(category, String.valueOf(index)))
                .originalPrice(300)
                .stock(200)
                .build();
    }

    private String formatImageUrl(Category category, String identifier) {
        return String.format(
                "%s/%s/%s%s%s",
                S3_URL,
                category.name().toLowerCase(),
                category.name().toLowerCase(),
                identifier,
                PNG);
    }

    private void saveItems(List<Item> items) {
        itemRepository.saveAll(items);
    }
}
