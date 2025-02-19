package com.example.web2_3_ourtuft_be.global.exception.exceptions;

import com.example.web2_3_ourtuft_be.global.exception.messages.AccessDeniedMessages;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends GlobalException {
    public AccessDeniedException(AccessDeniedMessages message) {
        super(message.getMessage(), HttpStatus.FORBIDDEN);
    }
}
