package com.mockApi.api.controller;

import com.mockApi.api.dto.ProfileRequestDto;
import com.mockApi.api.dto.ProfileResponseDto;
import com.mockApi.api.dto.ResponseMessageDto;
import com.mockApi.api.global.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "🎫 프로필", description = "프로필 관련 API")
public class ProfileController {

  @Operation(summary = "마이 프로필 조회", description = "내 프로필을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/my")
  public ResponseEntity<GlobalResponse<ProfileResponseDto>> myProfile() {

    ProfileResponseDto.EquipsDto equips = new ProfileResponseDto.EquipsDto(1, 2, 3, 4, 5);
    ProfileResponseDto response =
        new ProfileResponseDto("tester1", "hello!", 4, 15, 465, 500, 93.0, equips);

    return ResponseEntity.ok().body(GlobalResponse.success(response));
  }

  @Operation(summary = "보유 아이템 조회", description = "내가 보유하고 있는 아이템을 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/my/items")
  public ResponseEntity<GlobalResponse<ProfileResponseDto.Inventory>> myItems() {

    List<Integer> hairs = List.of(1, 2, 3);
    List<Integer> eyes = List.of(3, 4);
    List<Integer> mouses = List.of(2, 5);
    List<Integer> skins = List.of(1, 2);
    List<Integer> colors = List.of(5);

    ProfileResponseDto.Inventory response =
        new ProfileResponseDto.Inventory(hairs, eyes, mouses, skins, colors);

    return ResponseEntity.ok().body(GlobalResponse.success(response));
  }

  @Operation(summary = "프로필 수정", description = "프로필 정보를 수정합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PutMapping("/my")
  public ResponseEntity<GlobalResponse<ResponseMessageDto>> updateProfile(
      @RequestBody ProfileRequestDto request) {
    String msg = validateProfile(request);
    if (msg != null) {
      return ResponseEntity.badRequest()
          .body(GlobalResponse.failed(HttpStatus.BAD_REQUEST, new ResponseMessageDto(msg)));
    }

    return ResponseEntity.ok().body(GlobalResponse.success(new ResponseMessageDto("프로필 변경 성공")));
  }

  private String validateProfile(ProfileRequestDto requestDto) {

    if (requestDto.getIntroduce() != null && requestDto.getIntroduce().length() > 100) {
      return "소개 글자 수는 최대 100자까지 가능합니다.";
    }
    if (requestDto.getHair() == 0
        || requestDto.getEye() == 0
        || requestDto.getMouse() == 0
        || requestDto.getSkin() == 0
        || requestDto.getNickColor() == 0) {
      return "아이템을 장착해야 합니다.";
    }

    return null; // 검증 통과
  }

  @Operation(summary = "닉네임 체크", description = "닉네임 중복 체크를 실행합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @PostMapping("/check/username")
  public ResponseEntity<GlobalResponse<ResponseMessageDto>> checkUserName(
      @RequestBody ProfileRequestDto.CheckUserName request) {
    String username = request.getUsername();

    if (username.length() < 2 || username.length() > 10) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(GlobalResponse.failed(HttpStatus.BAD_REQUEST, new ResponseMessageDto("글자 수 에러")));
    }

    if (username.equals("mock1")) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(GlobalResponse.failed(HttpStatus.CONFLICT, new ResponseMessageDto("이미 사용중인 이름")));
    }

    return ResponseEntity.ok()
        .body(GlobalResponse.success(new ResponseMessageDto("사용 가능한 닉네임입니다.")));
  }

  @Operation(summary = "유저 정보 조회", description = "유저의 정보를 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/users/{username}")
  public ResponseEntity<GlobalResponse<?>> getUserProfile(@PathVariable String username) {
    if (!Objects.equals(username, "mock1")) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
              GlobalResponse.failed(
                  HttpStatus.NOT_FOUND, new ResponseMessageDto("존재하지 않는 유저입니다.")));
    }

    ProfileResponseDto.EquipsDto equips = new ProfileResponseDto.EquipsDto(1, 2, 3, 4, 5);
    ProfileResponseDto response =
        new ProfileResponseDto(username, "hello!", 4, 15, 465, 500, 93.0, equips);

    return ResponseEntity.ok(GlobalResponse.success(response));
  }

  @Operation(summary = "내 포인트 조회", description = "나의 포인트를 조회합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "성공")})
  @GetMapping("/my/point")
  public ResponseEntity<GlobalResponse<Integer>> myPoint() {
    int point = 100;
    return ResponseEntity.ok().body(GlobalResponse.success(point));
  }
}
