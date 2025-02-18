package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import com.example.web2_3_ourtuft_be.global.exception.messages.DuplicatedMessages;
import org.springframework.http.HttpStatus;

public class DuplicatedException extends GlobalException {
  public DuplicatedException(DuplicatedMessages message) {
    super(message.getMessage(), HttpStatus.CONFLICT);
  }
}
