package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.PreviousDayPoint;
import com.example.web2_3_ourtuft_be.user.repository.PreviousDayPointRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreviousDayPointService {

    private final PreviousDayPointRepository previousDayPointRepository;

    public void savePreviousDayPoint(Long userId, int calculatedPoints) {
        PreviousDayPoint previousDayPoint =
                PreviousDayPoint.builder()
                        .userId(userId)
                        .points(calculatedPoints)
                        .recordDate(LocalDateTime.now().withHour(0).withMinute(0).withSecond(0))
                        .build();

        previousDayPointRepository.save(previousDayPoint);
    }

    public PreviousDayPoint getPreviousDayPoint(Long userId) {
        return previousDayPointRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.POINT));
    }
}
