package com.example.web2_3_ourtuft_be.discount.controller;

import com.example.web2_3_ourtuft_be.discount.dto.DiscountRequest;
import com.example.web2_3_ourtuft_be.discount.dto.DiscountResponse;
import com.example.web2_3_ourtuft_be.discount.service.DiscountService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
@Tag(name = "Discount", description = "할인 관련 API")
public class DiscountController {

    private final DiscountService discountService;

    @Operation(summary = "할인 목록 조회 API", description = "모든 할인 정보를 조회합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping
    public ResponseEntity<GlobalResponse<List<DiscountResponse>>> getDiscounts() {

        List<DiscountResponse> response = discountService.getDiscounts();

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "할인 등록 API", description = "새로운 할인 정보를 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping
    public ResponseEntity<GlobalResponse<DiscountResponse>> registerDiscount(
            @RequestBody DiscountRequest request) {

        DiscountResponse response = discountService.registerDiscount(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.created(response));
    }

    @Operation(summary = "할인 수정 API", description = "기존 할인 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "해당 할인이 존재하지 않습니다.")
    })
    @PutMapping("/{discountId}")
    public ResponseEntity<GlobalResponse<DiscountResponse>> updateDiscount(
            @PathVariable Long discountId, @RequestBody DiscountRequest request) {

        DiscountResponse response = discountService.updateDiscount(discountId, request);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "할인 삭제 API", description = "할인을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "해당 할인이 존재하지 않습니다.")
    })
    @DeleteMapping("/{discountId}")
    public ResponseEntity<GlobalResponse<String>> deleteDiscount(@PathVariable Long discountId) {
        discountService.deleteDiscount(discountId);
        return ResponseEntity.ok(GlobalResponse.success("할인 삭제 성공"));
    }
}
