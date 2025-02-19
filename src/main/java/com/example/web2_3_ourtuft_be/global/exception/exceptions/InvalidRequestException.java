package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import com.example.web2_3_ourtuft_be.global.exception.messages.InvalidRequestMessages;
import org.springframework.http.HttpStatus;

public class InvalidRequestException extends GlobalException {
    public InvalidRequestException(InvalidRequestMessages message) {
        super(message.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
