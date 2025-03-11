package com.example.web2_3_ourtuft_be.user.value;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.InvalidValueException;
import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class Nickname {
    @Column(name = "nickname")
    private final String value;

    public Nickname() {
        this.value = null;
    }

    public Nickname(String value) {
        this.value = value;
    }

    public Nickname changeNickname(String value) {
        validate(value);
        return new Nickname(value);
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
