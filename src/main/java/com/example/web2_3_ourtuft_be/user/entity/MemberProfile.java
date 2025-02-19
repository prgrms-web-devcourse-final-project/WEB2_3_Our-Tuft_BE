package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.global.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberProfile extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    private String nickname;
    private String introduction;
    private Long eyeItemId;
    private Long mouseItemId;
    private Long skinItemId;
    private Long nicknameItemId;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfile(
            Long eyeItemId,
            Long mouseItemId,
            Long skinItemId,
            Long nicknameItemId,
            String introduction) {
        this.eyeItemId = eyeItemId;
        this.mouseItemId = mouseItemId;
        this.skinItemId = skinItemId;
        this.nicknameItemId = nicknameItemId;
        this.introduction = introduction;
    }
}
