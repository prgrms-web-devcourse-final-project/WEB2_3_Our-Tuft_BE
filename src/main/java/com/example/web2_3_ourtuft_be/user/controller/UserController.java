package com.example.web2_3_ourtuft_be.user.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.dto.UserInfoResponseDto;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final UserFacadeService userFacadeService;

    public UserController(UserService userService, UserFacadeService userFacadeService) {
        this.userService = userService;
        this.userFacadeService = userFacadeService;
    }

    @GetMapping("/myInfo")
    public ResponseEntity<GlobalResponse<UserInfoResponseDto>> getMyProfile() {

        UserInfoResponseDto response = userFacadeService.getMyInfo();
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<GlobalResponse<UserInfoResponseDto>> getUserInfo(
            @PathVariable Long userId) {

        UserInfoResponseDto responseDto = userFacadeService.getUserInfo(userId);
        return ResponseEntity.ok(GlobalResponse.success(responseDto));
    }
}
