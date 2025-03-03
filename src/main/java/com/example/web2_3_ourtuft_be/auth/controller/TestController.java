package com.example.web2_3_ourtuft_be.auth.controller;

import com.example.web2_3_ourtuft_be.auth.dto.*;
import com.example.web2_3_ourtuft_be.global.response.GlobalResponse;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.service.UserFacadeService;
import com.example.web2_3_ourtuft_be.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    // 프론트엔드 개발용 로그인 컨트롤러 (삭제예정)

    private final UserFacadeService userFacadeService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public TestController(
            UserFacadeService userFacadeService, UserService userService, JwtUtil jwtUtil) {
        this.userFacadeService = userFacadeService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/user")
    public ResponseEntity<GlobalResponse<User>> creatUser(@RequestBody CreateUserDto userDto) {

        User user = userFacadeService.registerUserForFE(userDto);
        return ResponseEntity.ok(GlobalResponse.success(user));
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<String>> login(@RequestBody LoginDto loginDto) {
        User user = userService.findUserBySocialId(loginDto.getSocialId());
        String accessToken =
                jwtUtil.createJwt(
                        "access",
                        user.getId(),
                        user.getName(),
                        user.getRole(),
                        24 * 60 * 60 * 1000L);
        return ResponseEntity.ok(GlobalResponse.success(accessToken));
    }
}
