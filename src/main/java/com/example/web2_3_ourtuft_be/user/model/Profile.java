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

    public Profile updateNickname(String newName) {
        this.nickname = nickname.changeNickname(newName);
        return this;
    }

    public Profile updateIntroAndAvatar(
            String introduction, Long eye, Long mouse, Long skin, Long nickname) {
        validIntroduction(introduction);
        this.introduction = introduction;
        this.avatar = avatar.update(eye, mouse, skin, nickname);
        return this;
    }

    private void validIntroduction(String introduction) {
        if (introduction.length() > 50) {
            throw new InvalidValueException(BadRequestMessages.INTRODUCE_TOO_LONG);
        }
    }
}
