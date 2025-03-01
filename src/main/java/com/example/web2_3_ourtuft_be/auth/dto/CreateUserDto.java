package com.example.web2_3_ourtuft_be.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateUserDto {
    // 프론트엔드 개발용 회원가입 DTO (삭제예정)
    private String providerId;
    private String name;
    private String email;
}
