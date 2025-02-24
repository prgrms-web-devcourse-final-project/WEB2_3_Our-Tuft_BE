package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.MemberExp;
import com.example.web2_3_ourtuft_be.user.repository.MemberExpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberExpService {

    private final MemberExpRepository expRepository;

    public MemberExp getMemberExp(Long userId) {
        return expRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }

    public void increaseExp(Long userId, int exp) {
        MemberExp memberExp = getMemberExp(userId);
        memberExp.increaseExp(exp);
        expRepository.save(memberExp);
    }

    public void createExp(Long userId) {
        MemberExp memberExp = new MemberExp(userId);
        expRepository.save(memberExp);
    }
}
