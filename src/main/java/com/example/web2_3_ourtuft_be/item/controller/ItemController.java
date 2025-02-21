package com.example.web2_3_ourtuft_be.item.controller;

import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.item.dto.ItemRequest;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items")
    public ResponseEntity<GlobalResponse<PageResponse<ItemResponse>>> getItems(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<ItemResponse> response = itemService.getItems(category, keyword, pageable);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @PostMapping("/item")
    public ResponseEntity<GlobalResponse<ItemResponse>> registerItem(
            @RequestBody ItemRequest request) {
        // 이미지 업로드 기능 추가하면 request 변경

        ItemResponse response = itemService.registerItem(request);

        return ResponseEntity.ok(GlobalResponse.created(response));
    }

    @PutMapping("/item/{itemId}")
    public ResponseEntity<GlobalResponse<ItemResponse>> updateItem(
            @PathVariable Long itemId, @RequestBody ItemRequest request) {

        ItemResponse response = itemService.updateItem(itemId, request);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<GlobalResponse<String>> deleteItem(@PathVariable Long itemId) {

        itemService.deleteItem(itemId);

        return ResponseEntity.ok(GlobalResponse.success("아이템 삭제 성공"));
    }
}
