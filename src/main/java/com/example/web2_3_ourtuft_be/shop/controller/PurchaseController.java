package com.example.web2_3_ourtuft_be.shop.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.shop.dto.PurchaseRequest;
import com.example.web2_3_ourtuft_be.shop.service.PurchaseService;
import com.example.web2_3_ourtuft_be.user.dto.WishItemRequestDto;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Shop", description = "상점 관련 API")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserFacadeService userFacadeService;

    @Operation(summary = "아이템 구매 API", description = "상점에서 아이템을 구매합니다.")
    @PostMapping("/purchase")
    public ResponseEntity<GlobalResponse<String>> purchase(@RequestBody PurchaseRequest request) {

        purchaseService.purchase(request.getItems());
        return ResponseEntity.ok(GlobalResponse.success("구매 성공"));
    }

    @Operation(summary = "아이템 찜 API", description = "상점에서 아이템을 찜합니다.")
    @PostMapping("/wishlist")
    public ResponseEntity<GlobalResponse<String>> addWishItem(
            @RequestBody WishItemRequestDto request,
            @AuthenticationPrincipal(expression = "user") User user) {
        userFacadeService.AddWishItem(user.getId(), request);
        return ResponseEntity.ok(GlobalResponse.success("상품을 위시리스트에 추가했습니다."));
    }

    @Operation(summary = "아이템 찜 취소 API", description = "상점 찜 목록에서 아이템을 찜 취소합니다.")
    @DeleteMapping("/wishlist/{itemId}")
    public ResponseEntity<GlobalResponse<String>> deleteWishItem(
            @PathVariable Long itemId, @AuthenticationPrincipal(expression = "user") User user) {
        userFacadeService.deleteWishItem(user.getId(), itemId);
        return ResponseEntity.ok(GlobalResponse.success("상품을 위시리스트에 추가했습니다."));
    }
}
