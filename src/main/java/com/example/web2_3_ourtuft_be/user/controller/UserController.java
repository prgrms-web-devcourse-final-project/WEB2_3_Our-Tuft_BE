package com.example.web2_3_ourtuft_be.user.controller;

import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.user.dto.*;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.InventoryService;
import com.example.web2_3_ourtuft_be.user.service.MemberPointService;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "😊 User", description = "회원 관련 API")
public class UserController {

    private final UserService userService;
    private final UserFacadeService userFacadeService;
    private final InventoryService inventoryService;
    private final MemberPointService memberPointService;

    /*
     *    @AuthenticationPrincipal(expression = "user") User user => Id, Role 존재
     */

    @Operation(summary = "내 정보 조회 API", description = "내 정보, 레벨, 승률, 아바타를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 회원입니다."),
        @ApiResponse(responseCode = "404", description = "아이템이 존재하지 않습니다..")
    })
    @GetMapping("/myInfo")
    public ResponseEntity<GlobalResponse<MyInfoResponseDto>> getMyProfile(
            @AuthenticationPrincipal(expression = "user") User user) {

        MyInfoResponseDto response = userFacadeService.getMyInfo(user.getId());
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "유저 정보 조회 API", description = "유저의 정보, 레벨, 승률, 아바타를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 회원입니다."),
        @ApiResponse(responseCode = "404", description = "아이템이 존재하지 않습니다..")
    })
    @GetMapping("/userInfo/{userId}")
    public ResponseEntity<GlobalResponse<UserInfoResponseDto>> getUserInfo(
            @PathVariable Long userId) {

        UserInfoResponseDto response = userFacadeService.getUserInfo(userId);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "내 정보 수정 API", description = "내 소개 및 아바타를 수정합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 회원입니다."),
        @ApiResponse(responseCode = "404", description = "아이템이 존재하지 않습니다.")
    })
    @PutMapping("/myInfo")
    public ResponseEntity<GlobalResponse<MyInfoResponseDto>> updateMyInfo(
            @RequestBody UserInfoRequestDto request,
            @AuthenticationPrincipal(expression = "user") User user) {

        MyInfoResponseDto response = userFacadeService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "닉네임 변경 API", description = "닉네임을 변경합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 회원입니다."),
        @ApiResponse(responseCode = "409", description = "이미 등록된 닉네임입니다.")
    })
    @PutMapping("/myInfo/nickname")
    public ResponseEntity<GlobalResponse<NickNameResponseDto>> changeNickname(
            @RequestBody NickNameRequestDto request,
            @AuthenticationPrincipal(expression = "user") User user) {
        NickNameResponseDto response = userFacadeService.changeNickName(user.getId(), request);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "내 아이템 목록 조회", description = "내 아이템을 항목 별로 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "아이템이 존재하지 않습니다.")
    })
    @GetMapping("/myInfo/items")
    public ResponseEntity<GlobalResponse<InventoryItemDto>> getMyItems(
            @AuthenticationPrincipal(expression = "user") User user) {
        InventoryItemDto response = inventoryService.getMyItems(user.getId());

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(summary = "내 포인트 조회", description = "로그인 한 회원의 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "포인트가 존재하지 않습니다.")
    })
    @GetMapping("/myInfo/points")
    public ResponseEntity<GlobalResponse<MyPointsResponseDto>> getMyPoints() {
        MyPointsResponseDto response = memberPointService.getMyPoints();

        return ResponseEntity.ok(GlobalResponse.success(response));
    }

    @Operation(
            summary = "컨텍스트에 등록된 회원 조회 API",
            description = "엑세스 토큰의 서명을 검증 후 컨텍스트에 저장한 회원의 정보(Id, Role)을 불러옵니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "401", description = "인증에 실패하였습니다."),
        @ApiResponse(responseCode = "403", description = "접근이 거부되었습니다.")
    })
    @GetMapping("/user")
    public ResponseEntity<GlobalResponse<UserResponse.GetUserByContext>> getUserByContext(
            @AuthenticationPrincipal(expression = "user") User user) {
        return ResponseEntity.ok(
                GlobalResponse.success(
                        new UserResponse.GetUserByContext(
                                user.getId(), user.getName(), user.getRole())));
    }

    // 게임종료 후, 경험치와 포인트를 받아 올리는 api, 아마 게임 쪽에서 메서드를 호출해서 처리할 듯( 임시로 작성해뒀습니다. )
    @Operation(summary = "보상 획득 API", description = "게임 후 얻은 경험치와 포인트를 올려줍니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "포인트가 존재하지 않습니다.")
    })
    @PostMapping("/reward")
    public ResponseEntity<GlobalResponse<RewardDto>> reward(
            @RequestBody RewardDto request,
            @AuthenticationPrincipal(expression = "user") User user) {

        RewardDto response = userFacadeService.reward(user.getId(), request);
        return ResponseEntity.ok(GlobalResponse.success(response));
    }
}
