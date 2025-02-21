package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotFoundMessages {
    USER("존재하지 않는 회원입니다."),
    ROOM_ID("해당 ID의 방을 찾을 수 없습니다."),
    ROOM_NAME("해당 이름을 포함하는 방을 찾을 수 없습니다."),
    ITEM("아이템이 존재하지 않습니다.");

    private final String message;
}
