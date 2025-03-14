package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.auth.dto.CreateUserDto;
import com.example.web2_3_ourtuft_be.auth.dto.OAuth2Response;
import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.*;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
import com.example.web2_3_ourtuft_be.user.model.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    private final MemberPointService pointService;
    private final WishlistItemService wishlistItemService;
    private final InventoryService inventoryService;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(Long userId) {

        User user = userService.getUser(userId);
        Profile profile = user.getProfile();
        MemberRecord record = recordService.getRecord(user.getId());
        MemberExp exp = expService.getMemberExp(user.getId());

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
    public MyInfoResponseDto getMyInfo(Long userId) {
        UserInfoResponseDto userInfo = getUserInfo(userId);
        return new MyInfoResponseDto(userId, userInfo);
    }

    @Transactional
    public UserInfoResponseDto updateProfile(Long userId, UserInfoRequestDto request) {

        User user = userService.getUser(userId);

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

        profileService.updateMemberProfile(user, request.getIntroduction(), equipItems);

        return getUserInfo(user.getId());
    }

    @Transactional
    public NickNameResponseDto changeNickName(Long userId, NickNameRequestDto request) {
        User user = userService.getUser(userId);
        String nickname = profileService.changeNickname(user, request.getNickName());

        String accessToken =
                jwtUtil.createJwt(
                        "access", user.getId(), user.getNickname(), user.getRole(), 3600000L);

        return new NickNameResponseDto(nickname, accessToken);
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
        profileService.createProfile(newUser);
        memberPointService.createPoint(userId);
        memberRecordService.createRecord(userId);
        expService.createExp(userId);
        inventoryService.registerDefaultItem(userId);
        return newUser;
    }

    public RewardDto reward(Long userId, RewardDto request) {
        int exp = expService.increaseExp(userId, request.getExp());
        pointService.updatePoints(
                userId, request.getPoints(), PointChangeType.INCREASE, PointChangeReason.REWARD);
        int points = pointService.getPoint(userId).getPoints();

        return new RewardDto(exp, points);
    }

    @Transactional
    public void AddWishItem(Long userId, Long itemId) {
        itemService.validItemId(itemId);
        wishlistItemService.addItem(userId, itemId);
    }

    @Transactional
    public void deleteWishItem(Long userId, Long itemId) {
        wishlistItemService.deleteWishItem(userId, itemId);
    }

    public PageResponse<ItemResponse> getWishItems(Long userId, Pageable pageable) {
        List<Long> wishItemsId = wishlistItemService.getWishItemIds(userId);
        return itemService.getItemInfoByWishlist(wishItemsId, pageable);
    }

    // 프론트엔드 개발용 (소셜 구현되면 삭제 예정)
    @Transactional
    public User registerUserForFE(CreateUserDto userInfo) {
        User exist = userService.findUserBySocialId(userInfo.getProviderId());
        if (exist != null) {
            throw new DuplicatedException(DuplicatedMessages.EMAIL);
        }
        User newUser = userService.createUserForFE(userInfo);
        Long userId = newUser.getId();
        profileService.createProfile(newUser);
        memberPointService.createPoint(userId);
        memberRecordService.createRecord(userId);
        expService.createExp(userId);
        inventoryService.registerDefaultItem(userId);
        return newUser;
    }
}
