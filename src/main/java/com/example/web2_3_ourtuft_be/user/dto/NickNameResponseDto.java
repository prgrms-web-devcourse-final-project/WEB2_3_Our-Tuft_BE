package com.example.web2_3_ourtuft_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NickNameResponseDto {
    private String nickName;
    private String accessToken;
}
