package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.auth.dto.CreateUserDto;
import com.example.web2_3_ourtuft_be.auth.dto.OAuth2Response;
import com.example.web2_3_ourtuft_be.common.PageResponse;
import com.example.web2_3_ourtuft_be.global.exception.exceptions.DuplicatedException;
import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import com.example.web2_3_ourtuft_be.item.dto.ItemResponse;
import com.example.web2_3_ourtuft_be.item.service.ItemService;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.*;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeReason;
import com.example.web2_3_ourtuft_be.user.entity.enums.PointChangeType;
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

        //TODO : 빌더패턴 도입 검토 (아이템이 없을시 Response 생성 오류 발생)
        return new UserInfoResponseDto(profile, record, exp, eye, mouse, skin, nickColor);
    }

    @Transactional(readOnly = true)
    public UserInfoResponseDto getMyInfo(Long userId) {
        return getUserInfo(userId);
    }

    @Transactional
    public UserInfoResponseDto updateProfile(Long userId, UserInfoRequestDto request) {

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

        return getMyInfo(userId);
    }

    @Transactional
    public NickNameResponseDto changeNickName(Long userId, NickNameRequestDto request) {
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

    public RewardDto reward(Long userId, RewardDto request) {
        int exp = expService.increaseExp(userId, request.getExp());
        // TODO: UserId 로직 변경하면서 points 가져오는 부분 중복 제거 예정
        pointService.updatePoints(
                userId, request.getPoints(), PointChangeType.INCREASE, PointChangeReason.REWARD);
        int points = pointService.getPoint(userId).getPoints();

        return new RewardDto(exp, points);
    }

    @Transactional
    public void AddWishItem(Long userId, WishItemRequestDto request) {
        itemService.validItemId(request.getItemId());
        wishlistItemService.addItem(userId, request.getItemId());
    }

    // TODO : 찜 페이지에서 찜 아이템을 취소한다면 새로운 찜 목록을 반환해야 할 것 같음, 응답값 수정 필요한지 논의
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
        profileService.createProfile(userId);
        memberPointService.createPoint(userId);
        memberRecordService.createRecord(userId);
        expService.createExp(userId);
        inventoryService.registerDefaultItem(userId);
        return newUser;
    }
}
