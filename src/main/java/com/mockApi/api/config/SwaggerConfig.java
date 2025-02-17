package com.mockApi.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Quiz API", version = "1.0", description = "실시간 퀴즈 대결 API 명세"))
public class SwaggerConfig {}
