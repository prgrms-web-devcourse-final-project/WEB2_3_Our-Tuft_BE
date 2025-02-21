package com.example.web2_3_ourtuft_be.user.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.InventoryService;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import com.example.web2_3_ourtuft_be.user.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

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

    @GetMapping("/user")
    public ResponseEntity<GlobalResponse<UserResponse.GetUserByContext>> getUserByContext(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(
                GlobalResponse.success(
                        new UserResponse.GetUserByContext(user.getId(), user.getRole())));
    }

    // 게임종료 후, 경험치와 포인트를 받아 올리는 api, 아마 게임 쪽에서 메서드를 호출해서 처리할 듯( 임시로 작성해뒀습니다. )
    @PostMapping("/reward")
    public ResponseEntity<GlobalResponse<RewardDto>> reward(
            @RequestBody RewardDto request) {

        RewardDto response = userFacadeService.reward(request);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
