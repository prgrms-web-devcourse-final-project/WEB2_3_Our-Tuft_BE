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
                            새로 발급받은 엑세스 토큰은 응답 헤더, 리프레쉬 토큰은 브라우저 쿠키에 담습니다.
                            프론트엔드에서 요청 시 credentials 를 통해 쿠키를 포함시켜야 합니다.
                            백엔드에서 테스트 시 스웨거의 Authorize 를 해제해야 합니다.
                            """)
    @ApiResponses({@ApiResponse(responseCode = "201", description = "성공")})
    @PostMapping("/reissue")
    public ResponseEntity<GlobalResponse<Void>> reissue(
            HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.created(authService.reissue(request, response)));
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
}
