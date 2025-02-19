package com.example.web2_3_ourtuft_be.auth.dto;

import com.example.web2_3_ourtuft_be.user.entity.enums.Provider;

public interface OAuth2Response {

    Provider getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
