package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadRequestMessages {
    NICKNAME_LENGTH("닉네임은 2자 이상 10자 이하여야 합니다."),
    NICKNAME_FORMAT("닉네임은 한글, 영어, 숫자만 가능합니다."),
    ROOM_PASSWORD("비공개방은 비밀번호를 반드시 입력해야 합니다."),
    ROOM_DUPLICATED_NAME("이미 존재하는 방 제목입니다."),
    ROOM_PASSWORD_LENGTH("비밀번호는 4자리여야 합니다."),
    ROOM_PASSWORD_FORMAT("비밀번호는 숫자만 입력할 수 있습니다."),
    ROOM_PASSWORD_DISCLOSURE("공개 방에는 비밀번호를 입력할 수 없습니다."),
    ROOM_PASSWORD_WRONG("비밀번호가 맞지 않습니다.");

    private final String message;
}
