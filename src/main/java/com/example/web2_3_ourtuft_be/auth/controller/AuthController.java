package com.example.web2_3_ourtuft_be.auth.controller;

import com.example.web2_3_ourtuft_be.auth.service.AuthService;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "👀 Auth", description = "사용자 인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "토큰 재발급 API",
            description =
                    """
                            새로 발급받은 엑세스 토큰은 응답 헤더, 리프레쉬 토큰은 브라우저 쿠키 및 레디스에 저장됩니다..
                            클라이언트 요청 시, withCredentials 를 통해 쿠키를 포함시켜야 합니다.
                            스웨거에서 테스트할 때, Authorize 를 해제해야 합니다(엑세스 토큰 만료 시).
                            """)
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "성공"),
        @ApiResponse(responseCode = "401", description = "리프레쉬 토큰을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "401", description = "리프레쉬 토큰이 만료되었습니다."),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레쉬 토큰입니다.")
    })
    @PostMapping("/reissue")
    public ResponseEntity<GlobalResponse<Void>> reissue(
            HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.created(null));
    }

    @Operation(
            summary = "카카오 로그인 경로",
            description =
                    """
                                카카오 로그인 경로입니다. 해당 사이트에서 로그인 후 리다이렉트 되는 URL의 엑세스 토큰을 활용하면 됩니다.
                                http://localhost:8080/oauth2/authorization/kakao
                                """)
    @GetMapping("/kakao/이거 진짜 경로 아임니다")
    public String kakaoLogin() {
        return "http://localhost:8080/oauth2/authorization/kakao";
    }

    @Operation(
            summary = "구글 로그인 경로",
            description =
                    """
                                구글 로그인 경로입니다. 해당 사이트에서 로그인 후 리다이렉트 되는 URL의 엑세스 토큰을 활용하면 됩니다.
                                http://localhost:8080/oauth2/authorization/google
                                """)
    @GetMapping("/google/이거 진짜 경로 아임니다")
    public String googleLogin() {
        return "http://localhost:8080/oauth2/authorization/google";
    }

    @Operation(summary = "로그아웃 API", description = "레디스의 리프레쉬 토큰과 쿠키를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "401", description = "리프레쉬 토큰을 찾을 수 없습니다."),
        @ApiResponse(responseCode = "401", description = "쿠키를 찾을 수 없습니다."),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 리프레쉬 토큰입니다.")
    })
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponse<Void>> logout(
            HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.ok(GlobalResponse.success(null));
    }
}
