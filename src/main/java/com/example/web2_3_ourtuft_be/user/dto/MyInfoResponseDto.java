package com.example.web2_3_ourtuft_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyInfoResponseDto {
    private Long userId;
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

    public MyInfoResponseDto(Long userId, UserInfoResponseDto userInfo) {
        this.userId = userId;
        this.nickname = userInfo.getNickname();
        this.introduction = userInfo.getIntroduction();
        this.exp = userInfo.getExp();
        this.progress = userInfo.getProgress();
        this.level = userInfo.getLevel();
        this.totalGames = userInfo.getTotalGames();
        this.wins = userInfo.getWins();
        this.winRate = userInfo.getWinRate();
        this.eye = userInfo.getEye();
        this.mouth = userInfo.getMouth();
        this.skin = userInfo.getSkin();
        this.nickColor = userInfo.getNickColor();
    }
}
