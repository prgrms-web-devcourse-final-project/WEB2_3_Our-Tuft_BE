package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.entity.PointHistory;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.repository.MemberPointRepository;
import com.example.web2_3_ourtuft_be.user.repository.PointHistoryRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;
    private final MemberPointRepository memberPointRepository;

    @Transactional
    public void savePointHistory(Long memberPointId, int amount, PointChangeReason reason) {
        PointHistory pointHistory =
                PointHistory.builder()
                        .memberPointId(memberPointId)
                        .pointChange(amount)
                        .reason(reason.name())
                        .usageTime(LocalDateTime.now())
                        .build();

        pointHistoryRepository.save(pointHistory);
    }

    public void validateUserPoints() {
        Long userId = 1L;

        MemberPoint memberPoint =
                memberPointRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));

        int storedPoints = memberPoint.getPoints();

        List<PointHistory> pointHistoryList =
                pointHistoryRepository.findByMemberPointId(memberPoint.getId());

        int calculatedPoints = storedPoints;
        for (PointHistory pointHistory : pointHistoryList) {
            calculatedPoints += pointHistory.getPointChange();
        }

        if (calculatedPoints != storedPoints) {
            throw new DuplicatedException(DuplicatedMessages.MISMATCH_POINT);
        }
    }
}
