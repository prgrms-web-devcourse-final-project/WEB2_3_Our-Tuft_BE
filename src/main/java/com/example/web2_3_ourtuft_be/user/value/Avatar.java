package com.example.web2_3_ourtuft_be.user.value;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Embeddable
@Getter
public class Avatar {
    private static final String BASE_URL = "https://team09-bucket.s3.ap-northeast-2.amazonaws.com/";
    private static final String PNG = ".png";
    private final Long eyeItemId;
    private final String eyeImage;
    private final Long mouseItemId;
    private final String mouseImage;
    private final Long skinItemId;
    private final String skinImage;
    private final Long nicknameItemId;
    private final String nicknameColor;

    public Avatar() {
        this.eyeItemId = 1L;
        this.eyeImage = BASE_URL + "eye/eye1" + PNG;
        this.mouseItemId = 11L;
        this.mouseImage = BASE_URL + "mouth/mouth" + PNG;
        this.skinItemId = 21L;
        this.skinImage = BASE_URL + "skin/skin1" + PNG;
        this.nicknameItemId = 31L;
        this.nicknameColor = "#000000";
    }

    @Builder
    private Avatar(
            Long eyeItemId,
            String eyeImage,
            Long mouseItemId,
            String mouseImage,
            Long skinItemId,
            String skinImage,
            Long nicknameItemId,
            String nicknameColor) {
        this.eyeItemId = eyeItemId;
        this.eyeImage = eyeImage;
        this.mouseItemId = mouseItemId;
        this.mouseImage = mouseImage;
        this.skinItemId = skinItemId;
        this.skinImage = skinImage;
        this.nicknameItemId = nicknameItemId;
        this.nicknameColor = nicknameColor;
    }

    public Avatar update(
            Long eyeItemId,
            String eyeImage,
            Long mouseItemId,
            String mouseImage,
            Long skinItemId,
            String skinImage,
            Long nicknameItemId,
            String nicknameColor) {
        return Avatar.builder()
                .eyeItemId(eyeItemId)
                .eyeImage(eyeImage)
                .mouseItemId(mouseItemId)
                .mouseImage(mouseImage)
                .skinItemId(skinItemId)
                .skinImage(skinImage)
                .nicknameItemId(nicknameItemId)
                .nicknameColor(nicknameColor)
                .build();
    }
}
