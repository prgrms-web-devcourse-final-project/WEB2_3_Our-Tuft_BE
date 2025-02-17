package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DuplicatedMessages {
  NICKNAME("이미 등록된 닉네임입니다."),
  EMAIL("이미 등록된 이메일입니다."),
  UNIQUE("UNIQUE 제약 조건 위배");

  private final String message;
}
