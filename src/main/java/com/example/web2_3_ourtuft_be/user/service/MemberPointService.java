package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidRequestException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.dto.MyPointsResponseDto;
import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.repository.MemberPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberPointService {

    private final MemberPointRepository memberPointRepository;

    public void updatePoints(Long userId, int amount) {
        MemberPoint memberPoint = getPoint(userId);

        int updatedPoints = memberPoint.getPoints() + amount;

        memberPoint.updatePoints(updatedPoints);
        memberPointRepository.save(memberPoint);
    }

    public MemberPoint getPoint(Long userId) {
        return memberPointRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }

    public MyPointsResponseDto getMyPoints() {
        Long userId = 1L;

        return new MyPointsResponseDto(getPoint(userId).getPoints());
    }
}
