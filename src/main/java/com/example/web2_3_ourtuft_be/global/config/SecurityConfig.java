package com.example.web2_3_ourtuft_be.global.config;

import com.example.web2_3_ourtuft_be.auth.service.CustomOAuth2UserService;
import com.example.web2_3_ourtuft_be.security.filter.CustomRequestFilter;
import com.example.web2_3_ourtuft_be.security.handler.CustomAccessDeniedHandler;
import com.example.web2_3_ourtuft_be.security.handler.CustomAuthenticationEntrypoint;
import com.example.web2_3_ourtuft_be.security.handler.CustomOAuthFailureHandler;
import com.example.web2_3_ourtuft_be.security.handler.CustomOAuthSuccessHandler;
import com.example.web2_3_ourtuft_be.security.util.JwtUtil;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomOAuth2UserService customOAuth2UserService;
    private static final String[] authUrls = {"/api/v1/auth/**"};
    private static final String[] allowUrls = {"api/v1/admin/**"};

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .headers(
                        headers ->
                                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(
                        new CustomRequestFilter(jwtUtil),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exceptionHandler ->
                                exceptionHandler
                                        .authenticationEntryPoint(
                                                new CustomAuthenticationEntrypoint())
                                        .accessDeniedHandler(new CustomAccessDeniedHandler()))
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers(authUrls)
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .oauth2Login(
                        oauth ->
                                oauth.userInfoEndpoint(
                                                userInfoEndpoint ->
                                                        userInfoEndpoint.userService(
                                                                customOAuth2UserService))
                                        .successHandler(new CustomOAuthSuccessHandler(jwtUtil))
                                        .failureHandler(new CustomOAuthFailureHandler()))
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
                web.ignoring()
                        .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                        .requestMatchers(new AntPathRequestMatcher("/css/**"))
                        .requestMatchers(new AntPathRequestMatcher("/js/**"))
                        .requestMatchers(new AntPathRequestMatcher("/image/**"))
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**"))
                        .requestMatchers(new AntPathRequestMatcher("/test"))
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**"))
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui.html"));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
