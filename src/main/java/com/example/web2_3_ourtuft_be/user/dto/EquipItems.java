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

    public String getEyeUrl() {
        return eye.getImageUrl();
    }

    public Long getMouseItemId() {
        return mouse.getItemId();
    }

    public String getMouseUrl() {
        return mouse.getImageUrl();
    }

    public Long getSkinItemId() {
        return skin.getItemId();
    }

    public String getSkinUrl() {
        return skin.getImageUrl();
    }

    public Long getNameItemId() {
        return name.getItemId();
    }

    public String getNameColor() {
        return name.getValue();
    }
}
