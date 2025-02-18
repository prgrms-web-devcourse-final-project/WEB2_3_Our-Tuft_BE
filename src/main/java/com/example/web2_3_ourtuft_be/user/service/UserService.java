package com.example.web2_3_ourtuft_be.user.service;

import com.example.web2_3_ourtuft_be.user.dto.MyPageResponseDto;
import com.example.web2_3_ourtuft_be.user.dto.UserItemDto;
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

  public MyPageResponseDto getMyPage() {
    // TODO: 추후 시큐리티 설정시 auth 에서 userId 가져오는 것으로 변경
    Long userId = 1L;
    MemberProfile profile = memberProfileRepository.findByUserId(userId);
    // TODO: 아이템 도메인 생성 후, 장착 아이템 id로 부터 imageUrl 조회하여 담는 것으로 변경
    UserItemDto eye = new UserItemDto(profile.getEyeItemId(), "1");
    UserItemDto mouse = new UserItemDto(profile.getMouseItemId(), "2");
    UserItemDto skin = new UserItemDto(profile.getSkinItemId(), "3");
    UserItemDto nickColor = new UserItemDto(profile.getNicknameItemId(), "red");

    return new MyPageResponseDto(profile, eye, mouse, skin, nickColor);
  }
}
