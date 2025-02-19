package com.example.web2_3_ourtuft_be.item.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.item.dto.ItemRequest;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/item")
    public ResponseEntity<GlobalResponse<String>> registerItem(@RequestBody ItemRequest request) {
        // 이미지 업로드 기능 추가하면 request 변경

        itemService.registerItem(request);

        return ResponseEntity.ok(GlobalResponse.success("아이템 생성 성공"));
    }
}
