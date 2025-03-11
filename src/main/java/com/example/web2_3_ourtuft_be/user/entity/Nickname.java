package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import lombok.Getter;

@Getter
public class Nickname {
    private final String nickname;

    public Nickname(String nickname) {
        validate(nickname);
        this.nickname = nickname;
    }

    private void validate(String nickname) {

        if (nickname == null || nickname.isEmpty()) {
            throw new InvalidValueException(BadRequestMessages.NICKNAME_LENGTH);
        }

        if (nickname.length() < 2 || nickname.length() > 10) {
            throw new InvalidValueException(BadRequestMessages.NICKNAME_LENGTH);
        }

        if (!nickname.matches("^[a-zA-Z0-9가-힣]+$")) {
            throw new InvalidValueException(BadRequestMessages.NICKNAME_FORMAT);
        }
    }
}
