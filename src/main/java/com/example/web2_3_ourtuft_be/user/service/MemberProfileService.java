package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.dto.EquipItems;
import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import com.example.web2_3_ourtuft_be.user.repository.MemberProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private final MemberProfileRepository profileRepository;

    public MemberProfile getMemberProfile(Long userId) {
        return profileRepository
                .findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
    }

    @Transactional
    public MemberProfile updateMemberProfile(Long userId, String introduction, EquipItems equipItems) {
        MemberProfile userProfile = profileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));

        userProfile.updateIntroduction(introduction);
        userProfile.updateEquipItem(equipItems.getEyeItemId(), equipItems.getMouseItemId(), equipItems.getSkinItemId(), equipItems.getNameItemId());
        return profileRepository.save(userProfile);
    }
}
