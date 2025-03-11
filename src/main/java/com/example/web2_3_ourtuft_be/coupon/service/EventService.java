package com.example.web2_3_ourtuft_be.coupon.service;

import com.example.web2_3_ourtuft_be.coupon.repository.EventRepository;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final MemberPointService memberPointService;
    private final EventRepository eventRepository;
    private static final int EVENT_QUANTITY = 20;

    public void apply(Long userId) {

        checkDuplicateApply(userId);
        checkSoldOut();

        eventRepository.add(userId);

        memberPointService.updatePoints(
                userId, 100, PointChangeType.INCREASE, PointChangeReason.EVENT);
    }

    private void checkSoldOut() {
        Long count = eventRepository.increment();

        if (count > EVENT_QUANTITY) {
            eventRepository.decrement();
            throw new RuntimeException("이벤트 마감");
        }
    }

    private void checkDuplicateApply(Long userId) {
        if (eventRepository.checkDuplicateApply(userId)) {
            throw new DuplicatedException(DuplicatedMessages.EVENT);
        }
    }
}
