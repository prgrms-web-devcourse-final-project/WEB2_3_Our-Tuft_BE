package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.dto.MyPointsResponseDto;
import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import com.example.web2_3_ourtuft_be.user.repository.MemberPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;
    private final PointHistoryService pointHistoryService;

    public MemberPoint getPoint(Long userId) {
        return memberPointRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }

    public MyPointsResponseDto getMyPoints(Long userId) {

        return new MyPointsResponseDto(getPoint(userId).getPoints());
    }

    public void createPoint(Long userId) {
        MemberPoint point = new MemberPoint(userId);
        memberPointRepository.save(point);
    }

    @Transactional
    public void updatePoints(
            Long userId, int amount, PointChangeType type, PointChangeReason reason) {
        MemberPoint memberPoint = getPoint(userId);

        memberPoint.updatePoints(type, amount);

        pointHistoryService.savePointHistory(memberPoint.getId(), amount, type, reason);
    }
}
