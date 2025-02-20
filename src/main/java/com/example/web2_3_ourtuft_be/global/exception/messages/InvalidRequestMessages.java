package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvalidRequestMessages {
    INVALID_COLOR_VALUE("유효한 색상 값을 입력하세요."),
    INVALID_IMAGE_VALUE("유효한 이미지 값을 입력하세요."),
    EMPTY_SEARCH_CONDITION("검색 조건을 입력하세요.")
    ;
    private final String message;
}
