package com.example.web2_3_ourtuft_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyInfoResponseDto {
    private String name;
    private UserInfoResponseDto userInfo;
}
