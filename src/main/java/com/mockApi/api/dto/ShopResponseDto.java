package com.mockApi.api.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ShopResponseDto {
    private final String username;
    private final int points;
    private final List<ItemResponseDto> items;

    public ShopResponseDto(String username, int points, List<ItemResponseDto> items) {
        this.username = username;
        this.points = points;
        this.items = items;
    }
}
