package com.example.web2_3_ourtuft_be.auth.dto;

public interface OAuth2Response {

  String getProvider();

  String getProviderId();

  String getEmail();

  String getName();
}
