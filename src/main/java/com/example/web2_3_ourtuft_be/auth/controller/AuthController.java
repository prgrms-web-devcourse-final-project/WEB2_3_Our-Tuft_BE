package com.example.web2_3_ourtuft_be.auth.controller;

import com.example.web2_3_ourtuft_be.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "👀 인증", description = "사용자 인증 관련 API")
public class AuthController {

    private final UserService userService;
}
