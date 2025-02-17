package com.mockApi.api.dto;

import java.util.List;
import lombok.Getter;

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
