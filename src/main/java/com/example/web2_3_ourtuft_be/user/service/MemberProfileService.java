package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import com.example.web2_3_ourtuft_be.user.repository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberProfileRepository profileRepository;

    public MemberProfile getMemberProfile(Long userId) {
        return profileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }
}
