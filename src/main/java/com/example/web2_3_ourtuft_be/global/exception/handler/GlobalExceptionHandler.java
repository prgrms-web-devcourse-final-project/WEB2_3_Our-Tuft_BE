package com.example.web2_3_ourtuft_be.global.exception.handler;

import com.example.web2_3_ourtuft_be.global.exception.exceptions.GlobalException;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {GlobalException.class})
    protected ResponseEntity<GlobalResponse<Void>> handleGlobalException(final GlobalException e) {
        log.error("handleGlobalException throw CustomException : {}", e.getMessage());
        return ResponseEntity.status(e.getStatus()).body(GlobalResponse.fail(e));
    }
}
