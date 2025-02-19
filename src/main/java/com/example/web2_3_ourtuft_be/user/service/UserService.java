package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.NotFoundException;
import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import com.example.web2_3_ourtuft_be.user.dto.ItemImageUrlDto;
import com.example.web2_3_ourtuft_be.user.dto.NickNameColorItemDto;
import com.example.web2_3_ourtuft_be.user.dto.UserProfileResponseDto;
import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import com.example.web2_3_ourtuft_be.user.repository.MemberProfileRepository;
import com.example.web2_3_ourtuft_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MemberProfileRepository memberProfileRepository;

    public UserProfileResponseDto getMyPage() {
        // TODO: 추후 시큐리티 설정시 auth 에서 userId 가져오는 것으로 변경
        Long userId = 1L;
        MemberProfile profile =
                memberProfileRepository
                        .findByUserId(userId)
                        .orElseThrow(() -> new NotFoundException(NotFoundMessages.USER));
        // TODO: 아이템 도메인 생성 후, 장착 아이템 id로 부터 imageUrl 조회하여 담는 것으로 변경
        ItemImageUrlDto eye = new ItemImageUrlDto(profile.getEyeItemId(), "1");
        ItemImageUrlDto mouse = new ItemImageUrlDto(profile.getMouseItemId(), "2");
        ItemImageUrlDto skin = new ItemImageUrlDto(profile.getSkinItemId(), "3");
        // TODO: FE api 명세 피드백에 따라 데이터 변경 예정 (예: rgb 값, 색이름...)
        NickNameColorItemDto nickColor =
                new NickNameColorItemDto(profile.getNicknameItemId(), "red");

        return new UserProfileResponseDto(profile, eye, mouse, skin, nickColor);
    }
}
