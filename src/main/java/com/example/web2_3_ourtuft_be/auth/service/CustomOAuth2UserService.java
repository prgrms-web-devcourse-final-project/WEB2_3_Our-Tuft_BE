package com.example.web2_3_ourtuft_be.auth.service;

import com.example.web2_3_ourtuft_be.auth.dto.CustomOAuth2User;
import com.example.web2_3_ourtuft_be.auth.dto.GoogleResponse;
import com.example.web2_3_ourtuft_be.auth.dto.KakaoResponse;
import com.example.web2_3_ourtuft_be.auth.dto.OAuth2Response;
import com.example.web2_3_ourtuft_be.user.entity.User;
import com.example.web2_3_ourtuft_be.user.entity.enums.Role;
import com.example.web2_3_ourtuft_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

    OAuth2User oAuth2User = super.loadUser(userRequest);

    String provider = userRequest.getClientRegistration().getRegistrationId();

    OAuth2Response oAuth2Response;
    if (provider.equals("kakao")) {
      oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
    } else if (provider.equals("google")) {
      oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
    } else {
      return null;
    }

    User user = findOrCreateUser(oAuth2Response);

    return new CustomOAuth2User(user, oAuth2User.getAttributes());
  }

  private User findOrCreateUser(OAuth2Response oAuth2Response) {
    User user = userRepository.findBySocialId(oAuth2Response.getProviderId()).orElse(null);

    if (user == null) {
      user =
          User.builder()
              .email(oAuth2Response.getEmail())
              .name(oAuth2Response.getName())
              .socialId(oAuth2Response.getProviderId())
              .provider(oAuth2Response.getProvider())
              .role(Role.ROLE_USER)
              .build();
      userRepository.save(user);
    }

    return user;
  }
}
