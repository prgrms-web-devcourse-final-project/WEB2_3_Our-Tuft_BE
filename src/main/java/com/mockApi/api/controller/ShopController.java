package com.mockApi.api.controller;

import com.mockApi.api.dto.ItemResponseDto;
import com.mockApi.api.dto.ResponseMessageDto;
import com.mockApi.api.dto.ShopResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

import java.util.List;

@RestController
public class ShopController {

    @GetMapping("/shop")
    public ResponseEntity<ShopResponseDto> getShopInfo() {

        String username = "tester1";
        int points = 1500;

        List<ItemResponseDto> items = List.of(
                new ItemResponseDto(1, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false),
                new ItemResponseDto(2, "졸린 눈", 630, "eye", "졸려 보이는 눈", 50, false),
                new ItemResponseDto(3, "녹색 피부", 130, "skin", "바이러스 먹은 피부", 1500, false)
        );

        ShopResponseDto response = new ShopResponseDto(username, points, items);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/shop/{itemId}")
    public ResponseEntity<ItemResponseDto> getShopInfo(@PathVariable int itemId) {

        ItemResponseDto item = new ItemResponseDto(itemId, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false);

        return ResponseEntity.ok(item);
    }

    @GetMapping("/shop/liked")
    public ResponseEntity<List<ItemResponseDto>> getLikedItems() {
        List<ItemResponseDto> likedItems = List.of(
                new ItemResponseDto(1, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, true),
                new ItemResponseDto(3, "녹색 피부", 130, "skin", "바이러스 먹은 피부", 1500, true)
        );

        return ResponseEntity.ok(likedItems);
    }

    @GetMapping("/shop/search")
    public ResponseEntity<?> searchItem(@RequestParam(name = "itemName") String itemName) {

        List<ItemResponseDto> allItems = List.of(
                new ItemResponseDto(1, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false),
                new ItemResponseDto(2, "졸린 눈", 630, "eye", "졸려 보이는 눈", 50, false),
                new ItemResponseDto(3, "녹색 피부", 130, "skin", "바이러스 먹은 피부", 1500, false)
        );

        List<ItemResponseDto> filteredItems = new ArrayList<>();
        for (ItemResponseDto item : allItems) {
            if (item.getItemName().contains(itemName)) {
                filteredItems.add(item);
            }
        }

        if(filteredItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessageDto("검색 결과가 없습니다."));
        }

        return ResponseEntity.ok(filteredItems);
    }

    @GetMapping("/shop/purchase/{itemId}")
    public ResponseEntity<?> purchaseItem(@PathVariable int itemId) {

        int userPoints = 30000;

        ItemResponseDto item = new ItemResponseDto(itemId, "갈색 머리카락", 500, "hair", "기본 갈색 머리카락", 100, false);

        if (userPoints < item.getItemPrice()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessageDto("포인트가 부족합니다."));
        }

        return ResponseEntity.ok(new ResponseMessageDto("구매 완료되었습니다."));

    }



}
