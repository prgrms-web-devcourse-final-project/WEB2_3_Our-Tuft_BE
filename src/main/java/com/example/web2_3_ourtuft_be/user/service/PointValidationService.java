package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.entity.PointHistory;
import com.example.web2_3_ourtuft_be.user.entity.PreviousDayPoint;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointValidationService {

    private final PreviousDayPointService previousDayPointService;
    private final PointHistoryService pointHistoryService;

    @Transactional
    public void validateUserPoints(MemberPoint memberPoint) {

        // 현재 보유한 포인트
        int storedPoints = getStoredPoints(memberPoint);

        // 전날 보유한 포인트
        int previousPoints = getPreviousDayPoints(memberPoint.getUserId());

        // 오늘 변동 포인트 계산
        int calculatedPoints = calculateTodayPoints(memberPoint);

        // 포인트 일치 여부
        verifyPoints(calculatedPoints, previousPoints, storedPoints, memberPoint.getUserId());

        // 검증된 포인트로 갱신
        updatePreviousDayPoints(storedPoints, memberPoint.getUserId());
    }

    private int getStoredPoints(MemberPoint memberPoint) {
        return memberPoint.getPoints();
    }

    private int getPreviousDayPoints(Long userId) {
        PreviousDayPoint previousDayPoint = previousDayPointService.getPreviousDayPoint(userId);
        return previousDayPoint.getPoints();
    }

    private int calculateTodayPoints(MemberPoint memberPoint) {

        List<PointHistory> pointHistoryList = pointHistoryService.findPointHistory(memberPoint);

        return pointHistoryList.stream()
                .mapToInt(
                        pointHistory ->
                                pointHistory.getType().equals("INCREASE")
                                        ? pointHistory.getPointChange()
                                        : -pointHistory.getPointChange())
                .sum();
    }

    private void verifyPoints(
            int calculatedPoints, int previousPoints, int storedPoints, Long userId) {
        if (calculatedPoints + previousPoints != storedPoints) {
            log.error(
                    "해당 사용자 포인트 매치 실패 {}: calculated {}, previous {}, stored {}",
                    userId,
                    calculatedPoints,
                    previousPoints,
                    storedPoints);
        }
    }

    private void updatePreviousDayPoints(int storedPoints, Long userId) {
        PreviousDayPoint previousDayPoint = previousDayPointService.getPreviousDayPoint(userId);
        previousDayPoint.updatePoints(storedPoints);
    }
}
