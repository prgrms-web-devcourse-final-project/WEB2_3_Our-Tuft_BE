package com.example.web2_3_ourtuft_be.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        servers =
                @Server(url = "https://hiq-lounge.duckdns.org", description = "https://hiq-lounge.duckdns.org 서버"),
        info =
                @Info(
                        title = "하이큐 API Docs",
                        version = "1.0",
                        description = "'여긴 내 구역이야' 팀의 최종 프로젝트 '하이큐' API 문서입니다."),
        security = @SecurityRequirement(name = "accessToken"))
@SecurityScheme(
        name = "accessToken",
        type = SecuritySchemeType.APIKEY,
        paramName = "Authorization",
        in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {}
