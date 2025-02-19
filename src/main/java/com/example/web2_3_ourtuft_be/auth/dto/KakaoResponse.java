package com.example.web2_3_ourtuft_be.auth.dto;

import com.example.web2_3_ourtuft_be.user.entity.enums.Provider;
import java.util.Map;

public class KakaoResponse implements OAuth2Response {

  private final Map<String, Object> attributes;
  private final Map<String, Object> kakaoAccount;
  private final Map<String, Object> profile;

  public KakaoResponse(Map<String, Object> attributes) {
    this.attributes = attributes;
    this.kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    this.profile = (Map<String, Object>) kakaoAccount.get("profile");
  }

  @Override
  public Provider getProvider() {
    return Provider.KAKAO;
  }

  @Override
  public String getProviderId() {
    return attributes.get("id").toString();
  }

  @Override
  public String getEmail() {
    return kakaoAccount.get("email").toString();
  }

  @Override
  public String getName() {
    return profile.get("nickname").toString();
  }
}
