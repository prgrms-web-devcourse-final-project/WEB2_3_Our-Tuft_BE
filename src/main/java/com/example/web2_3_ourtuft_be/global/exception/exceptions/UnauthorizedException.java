package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import com.example.web2_3_ourtuft_be.global.exception.messages.UnauthorizedMessages;
import org.springframework.http.HttpStatus;

public class UnauthorizedException extends GlobalException {
  public UnauthorizedException(UnauthorizedMessages message) {
    super(message.getMessage(), HttpStatus.UNAUTHORIZED);
  }
}
