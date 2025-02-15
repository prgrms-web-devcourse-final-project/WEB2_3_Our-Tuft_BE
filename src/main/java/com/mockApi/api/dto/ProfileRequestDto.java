package com.mockApi.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProfileRequestDto {
    private String username;
    private String introduce;
    private int hair;
    private int eye;
    private int mouse;
    private int skin;
    private int nickColor;
}
