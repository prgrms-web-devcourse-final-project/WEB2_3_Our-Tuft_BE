package com.mockApi.api.dto;


import lombok.Getter;

@Getter
public class ProfileResponseDto {

        private final String username;
        private final String introduce;
        private final int rank;
        private final int lev;
        private final int exp;
        private final int expForNextLevel;
        private final double progress;
        private final EquipsDto equips;

        public ProfileResponseDto(String username, String introduce, int rank, int lev, int exp, int expForNextLevel, double progress, EquipsDto equips) {
            this.username = username;
            this.introduce = introduce;
            this.rank = rank;
            this.lev = lev;
            this.exp = exp;
            this.expForNextLevel = expForNextLevel;
            this.progress = progress;
            this.equips = equips;
        }

        @Getter
        public static class EquipsDto {
            private final int hair;
            private final int eye;
            private final int mouse;
            private final int skin;
            private final int nickColor;

            public EquipsDto(int hair, int eye, int mouse, int skin, int nickColor) {
                this.hair = hair;
                this.eye = eye;
                this.mouse = mouse;
                this.skin = skin;
                this.nickColor = nickColor;
            }
        }

}
