package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.auth.dto.OAuth2Response;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.*;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacadeService {
    private final MemberProfileService profileService;
    private final MemberRecordService recordService;
    private final ItemService itemService;
    private final MemberExpService expService;
    private final UserService userService;
    private final MemberPointService memberPointService;
    private final MemberRecordService memberRecordService;
    private final InventoryService inventoryService;
    private final MemberPointService pointService;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {

        MemberProfile profile = profileService.getMemberProfile(userId);
        MemberRecord record = recordService.getRecord(userId);
        MemberExp exp = expService.getMemberExp(userId);

        // TODO: Item 생성 후 변경 예정
        ItemImageUrlDto eye =
                new ItemImageUrlDto(
                        profile.getEyeItemId(),
                        itemService.getItem(profile.getEyeItemId()).getImageUrl());
        ItemImageUrlDto mouse =
                new ItemImageUrlDto(
                        profile.getMouseItemId(),
                        itemService.getItem(profile.getMouseItemId()).getImageUrl());
        ItemImageUrlDto skin =
                new ItemImageUrlDto(
                        profile.getSkinItemId(),
                        itemService.getItem(profile.getSkinItemId()).getImageUrl());
        NickNameColorItemDto nickColor =
                new NickNameColorItemDto(
                        profile.getNicknameItemId(),
                        itemService.getItem(profile.getNicknameItemId()).getNickColor());

        return new UserInfoResponseDto(profile, record, exp, eye, mouse, skin, nickColor);
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
        ItemImageUrlDto eye =
                new ItemImageUrlDto(
                        request.getEye(), itemService.getItem(request.getEye()).getImageUrl());
        ItemImageUrlDto mouse =
                new ItemImageUrlDto(
                        request.getMouth(), itemService.getItem(request.getMouth()).getImageUrl());
        ItemImageUrlDto skin =
                new ItemImageUrlDto(
                        request.getSkin(), itemService.getItem(request.getSkin()).getImageUrl());
        NickNameColorItemDto nickColor =
                new NickNameColorItemDto(
                        request.getNickColor(),
                        itemService.getItem(request.getNickColor()).getNickColor());
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

    @Transactional
    public User findOrCreateUser(OAuth2Response oAuth2Response) {
        User user = userService.findUserBySocialId(oAuth2Response.getProviderId());

        if (user == null) {
            return registerUser(oAuth2Response);
        }
        return user;
    }

    @Transactional
    public User registerUser(OAuth2Response userInfo) {
        User newUser = userService.createUser(userInfo);
        Long userId = newUser.getId();
        profileService.createProfile(userId);
        memberPointService.createPoint(userId);
        memberRecordService.createRecord(userId);
        expService.createExp(userId);
        return newUser;
    }

    public RewardDto reward(RewardDto request) {
        Long userId = 1L;
        int exp = expService.increaseExp(userId, request.getExp());
        // TODO: UserId 로직 변경하면서 points 가져오는 부분 중복 제거 예정
        pointService.updatePoints(userId, request.getPoints(), PointChangeType.INCREASE, PointChangeReason.REWARD);
        int points = pointService.getPoint(userId).getPoints();

        return new RewardDto(exp, points);
    }
}
