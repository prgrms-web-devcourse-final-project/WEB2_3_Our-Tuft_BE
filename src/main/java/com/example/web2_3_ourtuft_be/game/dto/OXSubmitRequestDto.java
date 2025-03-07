package com.example.web2_3_ourtuft_be.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OXSubmitRequestDto {
    private Long roomId;
    private Long userId;
    private int round;
    private String answer;
}
