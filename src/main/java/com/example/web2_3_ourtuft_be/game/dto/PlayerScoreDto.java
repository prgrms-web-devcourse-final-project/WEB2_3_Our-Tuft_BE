package com.example.web2_3_ourtuft_be.game.dto;


public class PlayerScoreDto {
    private final String playerId;
    private final String nickName;
    private final int score;

    public PlayerScoreDto(String playerId, String nickName, int score) {
        this.playerId = playerId;
        this.nickName = nickName;
        this.score = score;
    }
}
