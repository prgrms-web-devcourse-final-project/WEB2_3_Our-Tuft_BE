package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import com.example.web2_3_ourtuft_be.user.model.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "USERS", uniqueConstraints = @UniqueConstraint(columnNames = "nickname"))
public class User extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "SOCIAL_ID", nullable = false, unique = true)
    private String socialId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PROVIDER", nullable = false)
    private String provider;

    @Column(name = "ROLE", nullable = false)
    private String role;

    @Embedded private Profile profile;

    public String getNickname() {
        return profile.getNickname();
    }

    public String getIntroduction() {
        return profile.getIntroduction();
    }

    public void changeNickname(String nickname) {
        profile.updateNickname(nickname);
    }

    public void updateProfile(
            String introduction,
            Long eyeId,
            String eyeImage,
            Long mouseId,
            String mouseImage,
            Long skinId,
            String skinImage,
            Long nicknameId,
            String nickColor) {
        this.profile =
                profile.updateIntroAndAvatar(
                        introduction,
                        eyeId,
                        eyeImage,
                        mouseId,
                        mouseImage,
                        skinId,
                        skinImage,
                        nicknameId,
                        nickColor);
    }

    public void createProfile(Profile profile) {
        this.profile = profile;
    }

    public String getEyeImage() {
        return profile.getEyeImage();
    }

    public String getSkinImage() {
        return profile.getSkinImage();
    }

    public String getMouseImage() {
        return profile.getMouseImage();
    }

    public String getNickNameColor() {
        return profile.getNicknameColor();
    }

    public static User to(Long id, String name, String role) {
        return User.builder().id(id).name(name).role(role).profile(new Profile(name)).build();
    }
}
