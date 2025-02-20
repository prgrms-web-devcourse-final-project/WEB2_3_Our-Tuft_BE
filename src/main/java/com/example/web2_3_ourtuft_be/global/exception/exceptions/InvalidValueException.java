package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import com.example.web2_3_ourtuft_be.global.exception.messages.BadRequestMessages;
import org.springframework.http.HttpStatus;

public class InvalidValueException extends GlobalException {
    public InvalidValueException(BadRequestMessages message) {
        super(message.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
