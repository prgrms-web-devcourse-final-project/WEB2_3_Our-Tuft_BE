package com.example.web2_3_ourtuft_be.coupon.controller;

import com.example.web2_3_ourtuft_be.coupon.dto.CouponRequest;
import com.example.web2_3_ourtuft_be.coupon.dto.CouponResponse;
import com.example.web2_3_ourtuft_be.coupon.service.CouponService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons")
@Tag(name = "Coupon", description = "쿠폰 관련 API")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "쿠폰 목록 조회 API", description = "모든 쿠폰을 조회합니다.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
    @GetMapping
    public ResponseEntity<GlobalResponse<List<CouponResponse>>> getCoupons() {

        List<CouponResponse> response = couponService.getCoupons();

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "쿠폰 등록 API", description = "새로운 쿠폰을 등록합니다.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping
    public ResponseEntity<GlobalResponse<CouponResponse>> registerCoupon(
            @RequestBody CouponRequest request) {

        CouponResponse response = couponService.registerCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.created(response));
    }

    @Operation(summary = "쿠폰 수정 API", description = "기존 쿠폰 정보를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "쿠폰이 존재하지 않습니다.")
    })
    @PutMapping("/{couponId}")
    public ResponseEntity<GlobalResponse<CouponResponse>> updateCoupon(
            @PathVariable Long couponId, @RequestBody CouponRequest request) {

        CouponResponse response = couponService.updateCoupon(couponId, request);

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "쿠폰 삭제 API", description = "쿠폰을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "쿠폰이 존재하지 않습니다.")
    })
    @DeleteMapping("/{couponId}")
    public ResponseEntity<GlobalResponse<String>> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseEntity.ok(GlobalResponse.success("쿠폰 삭제 성공"));
    }
}
