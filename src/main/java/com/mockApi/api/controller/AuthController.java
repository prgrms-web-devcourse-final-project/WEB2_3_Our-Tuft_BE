package com.mockApi.api.controller;

import com.mockApi.api.dto.ResponseMessageDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("auth/logout")
    public ResponseEntity<ResponseMessageDto> logout() {
        return ResponseEntity.ok(new ResponseMessageDto("로그아웃 성공"));
    }
}
