package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "member_profiles")
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

    @Column(unique = true)
    private String nickname;

    private String introduction;
    private Long eyeItemId;
    private Long mouseItemId;
    private Long skinItemId;
    private Long nicknameItemId;

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateEquipItem(
            Long eyeItemId, Long mouseItemId, Long skinItemId, Long nicknameItemId) {
        this.eyeItemId = eyeItemId;
        this.mouseItemId = mouseItemId;
        this.skinItemId = skinItemId;
        this.nicknameItemId = nicknameItemId;
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
