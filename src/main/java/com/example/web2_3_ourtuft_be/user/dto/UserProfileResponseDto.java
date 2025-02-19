package com.example.web2_3_ourtuft_be.user.dto;

import com.example.web2_3_ourtuft_be.user.entity.MemberProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserProfileResponseDto {
    private String nickname;
    private String introduction;
    private ItemImageUrlDto eye;
    private ItemImageUrlDto mouth;
    private ItemImageUrlDto skin;
    private NickNameColorItemDto nickColor;

    public UserProfileResponseDto(
            MemberProfile memberProfile,
            ItemImageUrlDto eye,
            ItemImageUrlDto mouth,
            ItemImageUrlDto skin,
            NickNameColorItemDto nickColor) {
        this.nickname = memberProfile.getNickname();
        this.introduction = memberProfile.getIntroduction();
        this.eye = eye;
        this.mouth = mouth;
        this.skin = skin;
        this.nickColor = nickColor;
    }
}
