package com.mockApi.api.controller;

import com.mockApi.api.dto.ResponseMessageDto;
import com.mockApi.api.global.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "👀 인증", description = "인증 관련 API")
public class AuthController {

  @Operation(summary = "로그아웃 API", description = "로그아웃을 진행합니다.")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "로그아웃 성공")})
  @GetMapping("/logout")
  public ResponseEntity<GlobalResponse<ResponseMessageDto>> logout() {
    return ResponseEntity.ok().body(GlobalResponse.success(new ResponseMessageDto("로그아웃 성공")));
  }
}
