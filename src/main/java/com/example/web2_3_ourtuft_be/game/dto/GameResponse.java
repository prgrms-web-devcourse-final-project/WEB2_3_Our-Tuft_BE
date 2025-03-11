package com.example.web2_3_ourtuft_be.game.dto;

public record GameResponse() {

    public record Scores(String username, String score) {
        public static Scores from(String username, String score) {
            return new Scores(username, score);
        }
    }
}
