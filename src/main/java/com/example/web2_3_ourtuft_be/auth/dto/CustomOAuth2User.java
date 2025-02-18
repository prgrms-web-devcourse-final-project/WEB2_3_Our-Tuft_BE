package com.example.web2_3_ourtuft_be.auth.dto;

import com.example.web2_3_ourtuft_be.user.entity.User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
public class CustomOAuth2User implements OAuth2User {

  private final User user;
  private final Map<String, Object> attributes;

  public CustomOAuth2User(User user, Map<String, Object> attributes) {
    this.user = user;
    this.attributes = attributes;
  }

  @Override
  public Map<String, Object> getAttributes() {
    return attributes;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
  }

  @Override
  public String getName() {
    return user.getName();
  }

  public String getSocialId() {
    return user.getSocialId();
  }
}
