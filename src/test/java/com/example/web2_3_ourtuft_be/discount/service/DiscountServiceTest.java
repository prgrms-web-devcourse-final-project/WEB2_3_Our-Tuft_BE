package com.example.web2_3_ourtuft_be.discount.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.web2_3_ourtuft_be.discount.dto.DiscountRequest;
import com.example.web2_3_ourtuft_be.discount.dto.DiscountResponse;
import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import com.example.web2_3_ourtuft_be.discount.entity.enums.DiscountType;
import com.example.web2_3_ourtuft_be.discount.repository.DiscountRepository;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock private DiscountRepository discountRepository;
    @Mock private ItemService itemService;

    @InjectMocks private DiscountService discountService;

    private Discount discount;
    private DiscountRequest discountRequest;

    @BeforeEach
    void setUp() {
        discount =
                Discount.builder()
                        .id(1L)
                        .type("PERCENTAGE")
                        .value(20)
                        .startDate(LocalDate.now())
                        .endDate(LocalDate.now().plusDays(1))
                        .build();
    }

    @DisplayName("모든 할인 정보를 조회")
    @Test
    void testGetDiscounts() {
        // given
        when(discountRepository.findAll()).thenReturn(List.of(discount));

        // when
        List<DiscountResponse> responses = discountService.getDiscounts();

        // then
        assertEquals(1, responses.size());
        assertEquals("PERCENTAGE", responses.get(0).getType());
    }

    @DisplayName("할인 정보를 등록")
    @Test
    void testRegisterDiscount() {
        // given
        discountRequest =
                new DiscountRequest(
                        DiscountType.PERCENTAGE,
                        20,
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        List.of(1L, 2L));

        when(discountRepository.save(any(Discount.class))).thenReturn(discount);

        // when
        DiscountResponse response = discountService.registerDiscount(discountRequest);

        // then
        assertNotNull(response);
        assertEquals("PERCENTAGE", response.getType());

        verify(itemService, times(1)).setDiscountId(discountRequest.getItemIds(), discount);
    }

    @DisplayName("할인 타입이 퍼센트인 경우 100을 넘는 숫자를 입력할 수 없다")
    @Test
    void testValidateInvalidPercentageValue() {
        DiscountRequest invalidRequest =
                new DiscountRequest(
                        DiscountType.PERCENTAGE,
                        150,
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        List.of(1L, 2L));

        assertThatThrownBy(() -> discountService.registerDiscount(invalidRequest))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(InvalidRequestMessages.PERCENTAGE_VALUE.getMessage());
    }

    @DisplayName("할인 날짜는 종료일이 시작일보다 앞설 수 없다")
    @Test
    void testValidateInvalidDateRange() {
        DiscountRequest invalidRequest =
                new DiscountRequest(
                        DiscountType.PERCENTAGE,
                        50,
                        LocalDate.now().plusDays(2),
                        LocalDate.now(),
                        List.of(1L, 2L));

        assertThatThrownBy(() -> discountService.registerDiscount(invalidRequest))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining(InvalidRequestMessages.INVALID_DATE.getMessage());
    }

    @DisplayName("할인 정보를 수정")
    @Test
    void testUpdateDiscount() {
        // given
        discountRequest =
                new DiscountRequest(
                        DiscountType.AMOUNT,
                        200,
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        List.of(1L, 2L));

        when(discountRepository.findById(1L)).thenReturn(Optional.of(discount));

        // when
        DiscountResponse response = discountService.updateDiscount(1L, discountRequest);

        // then
        assertNotNull(response);
        assertEquals("AMOUNT", response.getType());
        assertEquals(200, response.getValue());
    }

    @Test
    @DisplayName("할인 정보 수정 시 해당 할인이 없으면 실패")
    void testUpdateDiscountNotFound() {
        // given
        discountRequest =
                new DiscountRequest(
                        DiscountType.AMOUNT,
                        200,
                        LocalDate.now(),
                        LocalDate.now().plusDays(1),
                        List.of(1L, 2L));
        when(discountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(
                NotFoundException.class,
                () -> discountService.updateDiscount(999L, discountRequest));
    }

    @Test
    @DisplayName("할인 정보를 삭제")
    void testDeleteDiscount() {
        // given
        Long couponId = 1L;
        when(discountRepository.findById(couponId)).thenReturn(Optional.of(discount));

        // when
        discountService.deleteDiscount(couponId);

        // then
        verify(discountRepository).delete(discount);
    }

    @Test
    @DisplayName("할인 정보 삭제 시 해당 할인이 없으면 실패")
    void testDeleteDiscountNotFound() {
        // given
        when(discountRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () -> discountService.deleteDiscount(999L));
    }
}
