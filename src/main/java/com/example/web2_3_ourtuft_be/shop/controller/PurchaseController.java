package com.example.web2_3_ourtuft_be.shop.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.shop.dto.PurchaseRequest;
import com.example.web2_3_ourtuft_be.shop.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Shop", description = "상점 관련 API")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @Operation(summary = "아이템 구매 API", description = "상점에서 아이템을 구매합니다.")
    @PostMapping("/purchase")
    public ResponseEntity<GlobalResponse<String>> purchase(@RequestBody PurchaseRequest request) {

        purchaseService.purchase(request.getItems());
        return ResponseEntity.ok(GlobalResponse.success("구매 성공"));
    }
}
