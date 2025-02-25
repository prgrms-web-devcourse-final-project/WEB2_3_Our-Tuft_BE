package com.example.web2_3_ourtuft_be.coupon.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.web2_3_ourtuft_be.coupon.dto.CouponRequest;
import com.example.web2_3_ourtuft_be.coupon.dto.CouponResponse;
import com.example.web2_3_ourtuft_be.coupon.entity.Coupon;
import com.example.web2_3_ourtuft_be.coupon.repository.CouponRepository;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CouponServiceTest {

    @InjectMocks private CouponService couponService;

    @Mock private CouponRepository couponRepository;

    private Coupon coupon;
    private Coupon coupon2;

    @BeforeEach
    void setUp() {
        coupon =
                Coupon.builder()
                        .id(1L)
                        .description("쿠폰")
                        .discountAmount(2000)
                        .stock(5)
                        .minOrderAmount(10000)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(10))
                        .build();

        coupon2 =
                Coupon.builder()
                        .id(2L)
                        .description("쿠폰2")
                        .discountAmount(1000)
                        .stock(5)
                        .minOrderAmount(10000)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(10))
                        .build();
    }

    @Test
    @DisplayName("모든 쿠폰 목록을 조회")
    void testGetCoupons() {
        // given
        List<Coupon> coupons = List.of(coupon, coupon2);

        when(couponRepository.findAll()).thenReturn(coupons);

        // when
        List<CouponResponse> couponResponses = couponService.getCoupons();

        // then
        assertNotNull(couponResponses);
        assertEquals(2, couponResponses.size());
        assertEquals("쿠폰", couponResponses.get(0).getDescription());
        assertEquals("쿠폰2", couponResponses.get(1).getDescription());
    }

    @Test
    @DisplayName("쿠폰 등록 성공")
    void testRegisterCoupon() {
        // given
        CouponRequest request =
                new CouponRequest(
                        "쿠폰", 2000, 5, 10000, LocalDate.now(), LocalDate.now().plusDays(10));
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        // when
        CouponResponse couponResponse = couponService.registerCoupon(request);

        // then
        assertNotNull(couponResponse);
        assertEquals("쿠폰", couponResponse.getDescription());
        assertEquals(2000, couponResponse.getDiscountAmount());
    }

    @Test
    @DisplayName("쿠폰 수정 성공")
    void testUpdateCoupon() {
        // given
        CouponRequest request =
                new CouponRequest(
                        "수정된 쿠폰", 5000, 15, 20000, LocalDate.now(), LocalDate.now().plusDays(30));
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(coupon));

        // when
        CouponResponse couponResponse = couponService.updateCoupon(1L, request);

        // then
        assertNotNull(couponResponse);
        assertEquals("수정된 쿠폰", couponResponse.getDescription());
        assertEquals(5000, couponResponse.getDiscountAmount());
    }

    @Test
    @DisplayName("쿠폰 조회 시 쿠폰이 없으면 실패")
    void testGetCouponNotFound() {
        // given
        CouponRequest request =
                new CouponRequest(
                        "수정된 쿠폰", 5000, 15, 20000, LocalDate.now(), LocalDate.now().plusDays(30));
        when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> couponService.updateCoupon(999L, request));
    }

    @Test
    @DisplayName("쿠폰 삭제 테스트")
    void testDeleteCoupon() {
        // given
        Long couponId = 1L;
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        // when
        couponService.deleteCoupon(couponId);

        // then
        verify(couponRepository).delete(coupon);
    }

    @Test
    @DisplayName("쿠폰 삭제 시 쿠폰이 없으면 실패")
    void testDeleteCouponNotFound() {
        // given
        when(couponRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> couponService.deleteCoupon(999L));
    }
}
