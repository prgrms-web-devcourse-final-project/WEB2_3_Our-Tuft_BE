package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadRequestMessages {
    NICKNAME_LENGTH("닉네임은 2자 이상 10자 이하여야 합니다."),
    NICKNAME_FORMAT("닉네임은 한글, 영어, 숫자만 가능합니다.");

    private final String message;
}
