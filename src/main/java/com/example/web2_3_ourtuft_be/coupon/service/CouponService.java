package com.example.web2_3_ourtuft_be.coupon.service;

import com.example.web2_3_ourtuft_be.coupon.dto.CouponRequest;
import com.example.web2_3_ourtuft_be.coupon.dto.CouponResponse;
import com.example.web2_3_ourtuft_be.coupon.entity.Coupon;
import com.example.web2_3_ourtuft_be.coupon.repository.CouponRepository;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponse> getCoupons() {
        return couponRepository.findAll().stream().map(CouponResponse::from).toList();
    }

    public CouponResponse registerCoupon(CouponRequest request) {
        Coupon coupon =
                Coupon.builder()
                        .description(request.getDescription())
                        .discountAmount(request.getDiscountAmount())
                        .stock(request.getStock())
                        .minOrderAmount(request.getMinOrderAmount())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        return CouponResponse.from(savedCoupon);
    }

    public CouponResponse updateCoupon(Long couponId, CouponRequest request) {
        Coupon coupon =
                couponRepository
                        .findById(couponId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.COUPON));

        coupon.update(
                request.getDescription(),
                request.getDiscountAmount(),
                request.getStock(),
                request.getMinOrderAmount(),
                request.getStartDate(),
                request.getEndDate());

        return CouponResponse.from(coupon);
    }

    public void deleteCoupon(Long couponId) {

        Coupon coupon =
                couponRepository
                        .findById(couponId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.COUPON));

        couponRepository.delete(coupon);
    }
}
