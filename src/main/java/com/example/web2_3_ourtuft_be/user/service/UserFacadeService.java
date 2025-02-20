package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import com.example.web2_3_ourtuft_be.user.entity.MemberRecord;
import com.example.web2_3_ourtuft_be.user.entity.Nickname;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacadeService {
    private final MemberProfileService profileService;
    private final MemberRecordService recordService;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {

        MemberProfile profile = profileService.getMemberProfile(userId);
        MemberRecord record = recordService.getRecord(userId);
        // TODO: Item 생성 후 변경 예정
        ItemImageUrlDto eye = new ItemImageUrlDto(profile.getEyeItemId(), "1");
        ItemImageUrlDto mouse = new ItemImageUrlDto(profile.getMouseItemId(), "2");
        ItemImageUrlDto skin = new ItemImageUrlDto(profile.getSkinItemId(), "3");
        NickNameColorItemDto nickColor =
                new NickNameColorItemDto(profile.getNicknameItemId(), "#123456");

        return new UserInfoResponseDto(profile, record, eye, mouse, skin, nickColor);
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo() {
        // TODO: userId SecurityHolder 에서 가져옴
        Long userId = 1L;
        return getUserInfo(userId);
    }

    @Transactional
    public UserInfoResponseDto updateProfile(UserInfoRequestDto request) {
        // TODO: userId SecurityHolder 에서 가져옴
        Long userId = 1L;
        // TODO: Item 로직 생성 후 ItemService 에서 처리 예정
        ItemImageUrlDto eye = new ItemImageUrlDto(request.getEye(), "1");
        ItemImageUrlDto mouse = new ItemImageUrlDto(request.getMouth(), "2");
        ItemImageUrlDto skin = new ItemImageUrlDto(request.getMouth(), "3");
        NickNameColorItemDto nickColor =
                new NickNameColorItemDto(request.getNickColor(), "#123456");
        EquipItems equipItems = new EquipItems(eye, mouse, skin, nickColor);

        profileService.updateMemberProfile(userId, request.getIntroduction(), equipItems);

        return getMyInfo();
    }

    @Transactional
    public NickNameResponseDto changeNickName(NickNameRequestDto request) {
        // TODO: userId SecurityHolder 에서 가져옴
        Long userId = 1L;
        Nickname nickname = new Nickname(request.getNickName());
        return profileService.changeNickname(userId, nickname.getNickname());
    }
}
