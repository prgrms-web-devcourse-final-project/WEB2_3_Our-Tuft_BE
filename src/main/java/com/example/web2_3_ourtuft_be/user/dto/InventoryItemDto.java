package com.example.web2_3_ourtuft_be.user.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InventoryItemDto {
    List<ItemImageUrlDto> eye;
    List<ItemImageUrlDto> mouth;
    List<ItemImageUrlDto> skin;
    List<NickNameColorItemDto> nickColor;
}
