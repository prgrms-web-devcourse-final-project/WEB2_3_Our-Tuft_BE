package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "USERS")
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

    public static User to(Long id, String role) {
        return User.builder().id(id).role(role).build();
    }
}
