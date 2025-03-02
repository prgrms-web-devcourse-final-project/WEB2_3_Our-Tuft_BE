package com.example.web2_3_ourtuft_be.item.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemScheduler {

    private final ItemDiscountService itemDiscountService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateDiscounts() {
        LocalDate today = LocalDate.now();

        log.info("아이템에 할인 적용 시작");

        itemDiscountService.updateDiscountPrice(today);

        log.info("아이템에 할인 적용 완료");
    }
}
