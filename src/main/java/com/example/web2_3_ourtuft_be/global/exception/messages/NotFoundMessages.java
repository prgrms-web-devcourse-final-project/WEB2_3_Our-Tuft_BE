package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotFoundMessages {
  USER("존재하지 않는 회원입니다."),
  ;

  private final String message;
}
