package com.example.web2_3_ourtuft_be.user.entity;

import com.example.web2_3_ourtuft_be.common.BaseTime;
import com.example.web2_3_ourtuft_be.user.entity.enums.Role;
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

  @Column(name = "USERNAME", nullable = false, unique = true)
  private String username;

  @Column(name = "NAME", nullable = false)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(name = "ROLE", nullable = false)
  private Role role;
}
