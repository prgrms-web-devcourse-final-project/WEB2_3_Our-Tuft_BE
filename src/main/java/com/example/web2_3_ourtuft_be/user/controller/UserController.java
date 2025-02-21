package com.example.web2_3_ourtuft_be.user.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.service.InventoryService;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserFacadeService userFacadeService;
    private final InventoryService inventoryService;
    private final MemberPointService memberPointService;

    @GetMapping("/myInfo")
    public ResponseEntity<GlobalResponse<UserInfoResponseDto>> getMyProfile() {

        UserInfoResponseDto response = userFacadeService.getMyInfo();
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<GlobalResponse<UserInfoResponseDto>> getUserInfo(
            @PathVariable Long userId) {

        UserInfoResponseDto response = userFacadeService.getUserInfo(userId);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @PutMapping("/myInfo")
    public ResponseEntity<GlobalResponse<UserInfoResponseDto>> updateMyInfo(
            @RequestBody UserInfoRequestDto request) {

        UserInfoResponseDto response = userFacadeService.updateProfile(request);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @PutMapping("/myInfo/nickname")
    public ResponseEntity<GlobalResponse<NickNameResponseDto>> changeNickname(
            @RequestBody NickNameRequestDto request) {
        NickNameResponseDto response = userFacadeService.changeNickName(request);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @GetMapping("/myInfo/items")
    public ResponseEntity<GlobalResponse<InventoryItemDto>> getMyItems() {
        InventoryItemDto response = inventoryService.getMyItems();

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @GetMapping("/myInfo/points")
    public ResponseEntity<GlobalResponse<MyPointsResponseDto>> getMyPoints() {
        MyPointsResponseDto response = memberPointService.getMyPoints();

        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
