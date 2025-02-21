package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.dto.MyPointsResponseDto;
import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.entity.PointHistory;
import com.example.web2_3_ourtuft_be.user.repository.MemberPointRepository;
import com.example.web2_3_ourtuft_be.user.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public MemberPoint getPoint(Long userId) {
        return memberPointRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }

    public MyPointsResponseDto getMyPoints() {
        Long userId = 1L;

        validateUserPoints();

        return new MyPointsResponseDto(getPoint(userId).getPoints());
    }

    public void updatePoints(Long userId, int amount) {
        MemberPoint memberPoint = getPoint(userId);

        int updatedPoints = memberPoint.getPoints() + amount;

        memberPoint.updatePoints(updatedPoints);
        memberPointRepository.save(memberPoint);
    }

    public void validateUserPoints() {
        Long userId = 1L;

        MemberPoint memberPoint = memberPointRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));

        int storedPoints = memberPoint.getPoints();

        List<PointHistory> pointHistoryList = pointHistoryRepository.findByMemberPointId(memberPoint.getId());

        int calculatedPoints = storedPoints;
        for (PointHistory pointHistory : pointHistoryList) {
            calculatedPoints += pointHistory.getPointChange();
        }

        if (calculatedPoints != storedPoints) {
            throw new DuplicatedException(DuplicatedMessages.MISMATCH_POINT);
        }
    }

}
