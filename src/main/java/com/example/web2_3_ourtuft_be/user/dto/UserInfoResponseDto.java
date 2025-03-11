package com.example.web2_3_ourtuft_be.user.dto;

import com.example.web2_3_ourtuft_be.user.entity.MemberExp;
import com.example.web2_3_ourtuft_be.user.entity.MemberRecord;
import com.example.web2_3_ourtuft_be.user.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String nickname;
    private String introduction;
    private int level;
    private int exp;
    private double progress;
    private int totalGames;
    private int wins;
    private double winRate;
    private ItemImageUrlDto eye;
    private ItemImageUrlDto mouth;
    private ItemImageUrlDto skin;
    private NickNameColorItemDto nickColor;

    public UserInfoResponseDto(
            Profile profile,
            MemberRecord record,
            MemberExp memberExp,
            ItemImageUrlDto eye,
            ItemImageUrlDto mouth,
            ItemImageUrlDto skin,
            NickNameColorItemDto nickColor) {
        this.nickname = profile.getNickname();
        this.introduction = profile.getIntroduction();
        this.exp = memberExp.getExp();
        this.progress = memberExp.getProgress();
        this.level = memberExp.getLevel();
        this.totalGames = record.getTotalGames();
        this.wins = record.getWinCount();
        this.winRate = record.getWinRate();
        this.eye = eye;
        this.mouth = mouth;
        this.skin = skin;
        this.nickColor = nickColor;
    }
}
