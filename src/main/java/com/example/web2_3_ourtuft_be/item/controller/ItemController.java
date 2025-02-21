package com.example.web2_3_ourtuft_be.item.controller;

import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.item.dto.ItemRequest;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
@Tag(name = "🕶️ Item", description = "아이템 관련 API")

public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "아이템 목록 검색 API", description = "카테고리, 키워드, 페이지에 맞는 아이템 목록을 검색합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping
    public ResponseEntity<GlobalResponse<PageResponse<ItemResponse>>> getItems(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);

        PageResponse<ItemResponse> response = itemService.getItems(category, keyword, pageable);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "아이템 등록 API", description = "아이템을 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping
    public ResponseEntity<GlobalResponse<ItemResponse>> registerItem(
            @RequestBody ItemRequest request) {
        // 이미지 업로드 기능 추가하면 request 변경

        ItemResponse response = itemService.registerItem(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.created(response));
    }

    @Operation(summary = "아이템 정보 수정 API", description = "아이템 정보를 수정합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @PutMapping("/{itemId}")
    public ResponseEntity<GlobalResponse<ItemResponse>> updateItem(
            @PathVariable Long itemId, @RequestBody ItemRequest request) {

        ItemResponse response = itemService.updateItem(itemId, request);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "아이템 삭제 API", description = "아이템을 삭제합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @DeleteMapping("/{itemId}")
    public ResponseEntity<GlobalResponse<String>> deleteItem(@PathVariable Long itemId) {

        itemService.deleteItem(itemId);

        return ResponseEntity.ok(GlobalResponse.success("아이템 삭제 성공"));
    }
}
