package com.example.web2_3_ourtuft_be.user.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.dto.UserProfileResponseDto;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/myInfo")
    public ResponseEntity<GlobalResponse<UserProfileResponseDto>> getMyProfile() {

        UserProfileResponseDto response = userService.getMyPage();
        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
