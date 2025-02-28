package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.user.entity.MemberPoint;
import com.example.web2_3_ourtuft_be.user.repository.MemberPointRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PointValidationScheduler {

    private final MemberPointRepository memberPointRepository;
    private final PointValidationService pointValidationService;

    @Scheduled(cron = "0 0 0 * * *")
    public void validateAllUsersPoints() {
        log.info("포인트 검증 시작");

        List<MemberPoint> allMemberPoints = memberPointRepository.findAll();

        for (MemberPoint memberPoint : allMemberPoints) {
            pointValidationService.validateUserPoints(memberPoint);
        }

        log.info("포인트 검증 완료");
    }
}
