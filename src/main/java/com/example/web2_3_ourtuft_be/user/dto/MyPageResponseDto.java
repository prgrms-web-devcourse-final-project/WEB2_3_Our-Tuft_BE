package com.example.web2_3_ourtuft_be.user.dto;

import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResponseDto {
  private String nickname;
  private String introduction;
  private UserItemDto eye;
  private UserItemDto mouth;
  private UserItemDto skin;
  private UserItemDto nickColor;

  public MyPageResponseDto(
      MemberProfile memberProfile,
      UserItemDto eye,
      UserItemDto mouth,
      UserItemDto skin,
      UserItemDto nickColor) {
    this.nickname = memberProfile.getNickname();
    this.introduction = memberProfile.getIntroduction();
    this.eye = eye;
    this.mouth = mouth;
    this.skin = skin;
    this.nickColor = nickColor;
  }
}
