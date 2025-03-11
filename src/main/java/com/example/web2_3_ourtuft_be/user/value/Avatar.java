package com.example.web2_3_ourtuft_be.user.value;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Embeddable
@Getter
public class Avatar {
    private final Long eyeItemId;
    private final Long mouseItemId;
    private final Long skinItemId;
    private final Long nicknameItemId;

    public Avatar() {
        this.eyeItemId = 1L;
        this.mouseItemId = 11L;
        this.skinItemId = 21L;
        this.nicknameItemId = 31L;
    }

    @Builder
    private Avatar(Long eyeItemId, Long mouseItemId, Long skinItemId, Long nicknameItemId) {
        this.eyeItemId = eyeItemId;
        this.mouseItemId = mouseItemId;
        this.skinItemId = skinItemId;
        this.nicknameItemId = nicknameItemId;
    }

    public Avatar update(Long eyeItemId, Long mouseItemId, Long skinItemId, Long nicknameItemId) {
        return Avatar.builder()
                .eyeItemId(eyeItemId)
                .mouseItemId(mouseItemId)
                .skinItemId(skinItemId)
                .nicknameItemId(nicknameItemId)
                .build();
    }
}
