package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvalidRequestMessages {
    INVALID_COLOR_VALUE("유효한 색상 값을 입력하세요."),
    INVALID_IMAGE_VALUE("유효한 이미지 값을 입력하세요."),
    EMPTY_SEARCH_CONDITION("검색 조건을 입력하세요."),
    INSUFFICIENT_POINTS("보유 포인트가 부족합니다."),
    INVALID_QUIZ_COUNT("퀴즈 리스트는 최소 1개 이상이어야 합니다."),
    PERCENTAGE_VALUE("퍼센트 할인 값은 100을 넘을 수 없습니다"),
    INVALID_DATE("시작일은 종료일보다 앞서야 합니다.");

    private final String message;
}
