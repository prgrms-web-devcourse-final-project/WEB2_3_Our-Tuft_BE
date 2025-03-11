package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DuplicatedMessages {
    NICKNAME("이미 등록된 닉네임입니다."),
    EMAIL("이미 등록된 이메일입니다."),
    UNIQUE("UNIQUE 제약 조건 위배"),
    CONFLICT("충돌 발생, 다시 시도해주세요"),
    MISMATCH_POINT("포인트 정보가 일치하지 않습니다."),
    WISH_ITEM("이미 찜한 상품입니다."),
    EVENT("이미 신청한 이벤트입니다.");

    private final String message;
}
