package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotFoundMessages {
    USER("존재하지 않는 회원입니다."),
    ROOM("아직 방이 만들어지지 않았습니다."),
    ROOM_ID("해당 ID의 방을 찾을 수 없습니다."),
    ROOM_NAME("해당 이름을 포함하는 방을 찾을 수 없습니다."),
    ITEM("아이템이 존재하지 않습니다."),
    NOT_FOUND_QUIZ_SET("해당 퀴즈세트를 찾을 수 없습니다."),
    COUPON("쿠폰이 존재하지 않습니다."),
    DISCOUNT("해당 할인이 존재하지 않습니다."),
    WISH_ITEM("찜한 상품이 존재하지 않습니다."),
    POINT("포인트가 존재하지 않습니다."),
    QUIZ("해당 ID의 퀴즈를 찾을 수 없습니다."),
    QUIZZES("해당 퀴즈세트에 퀴즈가 존재하지 않습니다."),
    NOT_FOUND_QUIZ("해당 퀴즈세트에 맞는 퀴즈를 찾을 수 없습니다.");

    private final String message;
}
