package com.example.gg.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ID, Password 문자열을 Base64로 인코딩하여 전달하는 구조
                .httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 조건별로 요청 허용/제한 설정
                .authorizeRequests()
//                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/healthcheck", "/api/**", "/email/**").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/user/**").hasRole("USER")
//                .anyRequest().denyAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        // 권한 문제가 발생했을 때 이 부분을 호출한다.
                        response.setStatus(403);
                        response.setCharacterEncoding("utf-8");
                        response.setContentType("text/html; charset=UTF-8");
                        response.getWriter().write("권한이 없는 사용자입니다.");
                    }
                })
                .authenticationEntryPoint(new AuthenticationEntryPoint() {
                    @Override
                    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                        // 인증문제가 발생했을 때 이 부분을 호출한다.
                        response.setStatus(401);
                        response.setCharacterEncoding("utf-8");
                        response.setContentType("text/html; charset=UTF-8");
                        response.getWriter().write("인증되지 않은 사용자입니다.");
                    }
                });

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.addAllowedOriginPattern("*");
//        configuration.addAllowedOrigin("https://lolonoa.site");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
