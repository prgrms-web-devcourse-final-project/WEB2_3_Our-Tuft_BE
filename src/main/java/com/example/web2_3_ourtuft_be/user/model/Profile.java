package com.example.web2_3_ourtuft_be.user.model;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import com.example.web2_3_ourtuft_be.user.value.Avatar;
import com.example.web2_3_ourtuft_be.user.value.Nickname;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Embeddable
public class Profile {
    @Embedded private Nickname nickname;
    private String introduction = "안녕하세요";
    @Embedded private Avatar avatar;

    public Profile(String nickname) {
        this.nickname = new Nickname(nickname);
        this.avatar = new Avatar();
    }

    public void updateNickname(String newName) {
        this.nickname = nickname.changeNickname(newName);
    }

    public Profile updateIntroAndAvatar(
            String introduction,
            Long eye,
            String eyeImage,
            Long mouse,
            String mouseImage,
            Long skin,
            String skinImage,
            Long nickname,
            String color) {
        if (introduction != null) {
            validIntroduction(introduction);
            this.introduction = introduction;
        }
        this.avatar =
                avatar.update(eye, eyeImage, mouse, mouseImage, skin, skinImage, nickname, color);
        return this;
    }

    public Long getEyeItemId() {
        return avatar.getEyeItemId();
    }

    public String getEyeImage() {
        return avatar.getEyeImage();
    }

    public Long getMouseItemId() {
        return avatar.getMouseItemId();
    }

    public String getMouseImage() {
        return avatar.getMouseImage();
    }

    public Long getSkinItemId() {
        return avatar.getSkinItemId();
    }

    public String getSkinImage() {
        return avatar.getSkinImage();
    }

    public Long getNicknameItemId() {
        return avatar.getNicknameItemId();
    }

    public String getNicknameColor() {
        return avatar.getNicknameColor();
    }

    public String getNickname() {
        return nickname.getValue();
    }

    private void validIntroduction(String introduction) {
        if (introduction.length() > 50) {
            throw new InvalidValueException(BadRequestMessages.INTRODUCE_TOO_LONG);
        }
    }
}
