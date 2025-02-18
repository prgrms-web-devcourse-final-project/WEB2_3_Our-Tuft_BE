package com.example.web2_3_ourtuft_be.auth.dto;

import com.example.web2_3_ourtuft_be.user.entity.enums.Provider;
import java.util.Map;

public class GoogleResponse implements OAuth2Response {

  private final Map<String, Object> attributes;

  public GoogleResponse(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  @Override
  public Provider getProvider() {
    return Provider.GOOGLE;
  }

  @Override
  public String getProviderId() {
    return attributes.get("sub").toString();
  }

  @Override
  public String getEmail() {
    return attributes.get("email").toString();
  }

  @Override
  public String getName() {
    return attributes.get("name").toString();
  }
}
