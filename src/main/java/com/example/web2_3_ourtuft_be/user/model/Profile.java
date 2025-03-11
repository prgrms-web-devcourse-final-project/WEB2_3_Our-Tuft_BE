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
            String introduction, Long eye, Long mouse, Long skin, Long nickname) {
        if (introduction != null) {
            validIntroduction(introduction);
            this.introduction = introduction;
        }
        this.avatar = avatar.update(eye, mouse, skin, nickname);
        return this;
    }

    public Long getEyeItemId() {
        return avatar.getEyeItemId();
    }

    public Long getMouseItemId() {
        return avatar.getMouseItemId();
    }

    public Long getSkinItemId() {
        return avatar.getSkinItemId();
    }

    public Long getNicknameItemId() {
        return avatar.getNicknameItemId();
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
