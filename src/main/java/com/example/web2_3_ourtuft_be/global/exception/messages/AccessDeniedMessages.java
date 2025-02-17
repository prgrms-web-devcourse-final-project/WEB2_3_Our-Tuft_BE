package com.example.web2_3_ourtuft_be.global.exception.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccessDeniedMessages {
  ADMIN_ONLY_ACCESS("해당 리소스는 관리자만 접근할 수 있습니다."),
  ;
  private final String message;
}
