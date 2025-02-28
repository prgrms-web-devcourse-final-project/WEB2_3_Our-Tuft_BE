package com.example.web2_3_ourtuft_be.discount.service;

import com.example.web2_3_ourtuft_be.discount.dto.DiscountRequest;
import com.example.web2_3_ourtuft_be.discount.dto.DiscountResponse;
import com.example.web2_3_ourtuft_be.discount.entity.Discount;
import com.example.web2_3_ourtuft_be.discount.entity.enums.DiscountType;
import com.example.web2_3_ourtuft_be.discount.repository.DiscountRepository;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    public List<DiscountResponse> getDiscounts() {
        return discountRepository.findAll().stream().map(DiscountResponse::from).toList();
    }

    public DiscountResponse registerDiscount(DiscountRequest request) {

        validate(request);

        Discount discount =
                Discount.builder()
                        .type(request.getType().name())
                        .value(request.getValue())
                        .startDate(request.getStartDate())
                        .endDate(request.getEndDate())
                        .build();

        Discount savedDiscount = discountRepository.save(discount);

        return DiscountResponse.from(savedDiscount);
    }

    public DiscountResponse updateDiscount(Long discountId, DiscountRequest request) {

        validate(request);

        Discount discount =
                discountRepository
                        .findById(discountId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.DISCOUNT));

        discount.update(
                request.getType().name(),
                request.getValue(),
                request.getStartDate(),
                request.getEndDate());

        return DiscountResponse.from(discount);
    }

    private void validate(DiscountRequest request) {
        if (request.getType() == DiscountType.PERCENTAGE && request.getValue() > 100) {
            throw new InvalidRequestException(InvalidRequestMessages.PERCENTAGE_VALUE);
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new InvalidRequestException(InvalidRequestMessages.INVALID_DATE);
        }
    }

    public void deleteDiscount(Long discountId) {
        Discount discount =
                discountRepository
                        .findById(discountId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.DISCOUNT));

        discountRepository.delete(discount);
    }

    public List<Discount> getDiscountByStartDate(LocalDate today) {
        return discountRepository.findByStartDate(today);
    }

    public List<Discount> getDiscountByEndDate(LocalDate today) {
        return discountRepository.findByEndDate(today);
    }
}
