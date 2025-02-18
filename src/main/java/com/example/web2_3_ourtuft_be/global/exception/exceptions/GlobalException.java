package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

  private final HttpStatus status;

  public GlobalException(final String message, final HttpStatus status) {
    super(message);
    this.status = status;
  }
}
