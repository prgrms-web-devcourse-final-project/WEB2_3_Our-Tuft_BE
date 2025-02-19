package com.example.web2_3_ourtuft_be.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Quiz API", version = "1.0", description = "실시간 퀴즈 API 명세"),
        security = @SecurityRequirement(name = "accessToken"))
@SecurityScheme(
        name = "accessToken",
        type = SecuritySchemeType.APIKEY,
        paramName = "access",
        in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {}
