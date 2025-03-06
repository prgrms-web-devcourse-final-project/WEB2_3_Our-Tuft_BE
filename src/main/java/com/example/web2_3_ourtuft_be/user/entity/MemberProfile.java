package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "member_profiles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberProfile extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(unique = true)
    private String nickname;

    private String introduction = "안녕하세요";

    private Long eyeItemId = 1L;

    private Long mouseItemId = 4L;

    private Long skinItemId = 7L;

    private Long nicknameItemId = 10L;

    public MemberProfile(Long userId) {
        this.userId = userId;
    }

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
