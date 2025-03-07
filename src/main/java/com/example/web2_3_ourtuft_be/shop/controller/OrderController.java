package com.example.web2_3_ourtuft_be.shop.controller;

import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.shop.dto.OrderListResponse;
import com.example.web2_3_ourtuft_be.shop.dto.OrderRequest;
import com.example.web2_3_ourtuft_be.shop.service.OrderFacadeService;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
@Tag(name = "Shop", description = "상점 관련 API")
public class OrderController {

    private final OrderFacadeService orderFacadeService;
    private final UserFacadeService userFacadeService;

    @Operation(summary = "아이템 구매 API", description = "상점에서 아이템을 구매합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "아이템이 존재하지 않습니다."),
        @ApiResponse(responseCode = "400", description = "보유 포인트가 부족합니다."),
    })
    @PostMapping("/order")
    public ResponseEntity<GlobalResponse<OrderListResponse>> order(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal(expression = "user") User user) {

        OrderListResponse response = orderFacadeService.order(user.getId(), request.getItems());
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "아이템 찜 API", description = "상점에서 아이템을 찜합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "409", description = "이미 찜한 상품입니다."),
    })
    @PostMapping("/wishlist/{itemId}")
    public ResponseEntity<GlobalResponse<String>> addWishItem(
            @PathVariable Long itemId, @AuthenticationPrincipal(expression = "user") User user) {
        userFacadeService.AddWishItem(user.getId(), itemId);
        return ResponseEntity.ok(GlobalResponse.success("상품을 위시리스트에 추가했습니다."));
    }

    @Operation(summary = "아이템 찜 취소 API", description = "상점 찜 목록에서 아이템을 찜 취소합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "찜한 상품이 존재하지 않습니다.")
    })
    @DeleteMapping("/wishlist/{itemId}")
    public ResponseEntity<GlobalResponse<String>> deleteWishItem(
            @PathVariable Long itemId, @AuthenticationPrincipal(expression = "user") User user) {
        userFacadeService.deleteWishItem(user.getId(), itemId);
        return ResponseEntity.ok(GlobalResponse.success("상품을 위시리스트에 추가했습니다."));
    }

    @Operation(summary = "아이템 찜 목록 API", description = "상점 찜 목록을 불러옵니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping("/wishlist")
    public ResponseEntity<GlobalResponse<PageResponse<ItemResponse>>> wishItemList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            @AuthenticationPrincipal(expression = "user") User user) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ItemResponse> response =
                userFacadeService.getWishItems(user.getId(), pageable);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
