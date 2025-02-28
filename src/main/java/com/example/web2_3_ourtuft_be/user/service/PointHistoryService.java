package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.entity.PointHistory;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import com.example.web2_3_ourtuft_be.user.repository.PointHistoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public void savePointHistory(
            Long memberPointId, int amount, PointChangeType type, PointChangeReason reason) {
        PointHistory pointHistory =
                PointHistory.builder()
                        .memberPointId(memberPointId)
                        .pointChange(amount)
                        .type(type.name())
                        .reason(reason.name())
                        .pointChangeTime(LocalDateTime.now())
                        .build();

        pointHistoryRepository.save(pointHistory);
    }

    public List<PointHistory> findPointHistory(MemberPoint memberPoint) {
        LocalDateTime startOfDay =
                LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);

        return pointHistoryRepository.findByMemberPointIdAndPointChangeTimeBetween(
                memberPoint.getId(), startOfDay, endOfDay);
    }
}
