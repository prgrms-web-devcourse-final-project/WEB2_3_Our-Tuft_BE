package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import com.example.web2_3_ourtuft_be.global.exception.messages.NotFoundMessages;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GlobalException {
  public NotFoundException(NotFoundMessages message) {
    super(message.getMessage(), HttpStatus.NOT_FOUND);
  }
}
