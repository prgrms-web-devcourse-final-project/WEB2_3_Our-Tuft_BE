package com.example.web2_3_ourtuft_be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EquipItems {
    private ItemImageUrlDto eye;
    private ItemImageUrlDto mouse;
    private ItemImageUrlDto skin;
    private NickNameColorItemDto name;

    public Long getEyeItemId() {
        return eye.getItemId();
    }

    public Long getMouseItemId() {
        return mouse.getItemId();
    }

    public Long getSkinItemId() {
        return skin.getItemId();
    }

    public Long getNameItemId() {
        return name.getItemId();
    }
}
