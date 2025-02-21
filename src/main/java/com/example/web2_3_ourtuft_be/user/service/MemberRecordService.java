package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.MemberRecord;
import com.example.web2_3_ourtuft_be.user.repository.MemberRecordRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberRecordService {

    private final MemberRecordRepository memberRecordRepository;

    @Transactional
    public void updateRecord(Long userId, boolean isWin) {
        try {
            MemberRecord record =
                    memberRecordRepository
                            .findById(userId)
                            .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));

            record.increaseTotalGames();
            if (isWin) {
                record.increaseWinCount();
            }

            memberRecordRepository.save(record);
        } catch (OptimisticLockException e) {
            // 충돌을 잡지 않는다면 아무일도 일어나지 않음, 즉, 사용자는 알 수 없음. 이거 더 좋을수도?
            throw new DuplicatedException(DuplicatedMessages.CONFLICT);
        }
    }

    public MemberRecord getRecord(Long userId) {
        return memberRecordRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }
}
