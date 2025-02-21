package com.example.web2_3_ourtuft_be.user.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/user")
    public ResponseEntity<GlobalResponse<UserResponse.GetUserByContext>> getUserByContext(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(
                GlobalResponse.success(
                        new UserResponse.GetUserByContext(user.getId(), user.getRole())));
    }
}
