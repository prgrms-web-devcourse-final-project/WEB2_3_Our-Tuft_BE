package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.user.dto.EquipItems;
import com.example.web2_3_ourtuft_be.user.dto.NickNameResponseDto;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.model.Profile;
import com.example.web2_3_ourtuft_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberProfileService {

    private static final String DEMETER = "_";
    private final UserRepository userRepository;

    @Transactional
    public void updateMemberProfile(User user, String introduction, EquipItems equipItems) {

        user.updateProfile(
                introduction,
                equipItems.getEyeItemId(),
                equipItems.getEyeUrl(),
                equipItems.getMouseItemId(),
                equipItems.getMouseUrl(),
                equipItems.getSkinItemId(),
                equipItems.getSkinUrl(),
                equipItems.getNameItemId(),
                equipItems.getNameColor());
    }

    @Transactional
    public NickNameResponseDto changeNickname(User user, String nickname) {
        duplicateNickname(nickname);
        user.changeNickname(nickname);

        return new NickNameResponseDto(user.getNickname());
    }

    public void duplicateNickname(String nickname) {
        boolean isExist = userRepository.existsByProfile_Nickname_Value(nickname);

        if (isExist) {
            throw new DuplicatedException(DuplicatedMessages.NICKNAME);
        }
    }

    public void createProfile(User user) {
        Profile profile = new Profile(user.getName() + DEMETER + user.getId());
        user.createProfile(profile);
    }
}
