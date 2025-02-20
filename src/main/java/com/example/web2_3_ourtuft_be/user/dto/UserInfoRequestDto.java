package com.example.web2_3_ourtuft_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInfoRequestDto {

    private String introduction;
    private Long eye;
    private Long mouth;
    private Long skin;
    private Long nickColor;
}
