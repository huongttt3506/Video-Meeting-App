package com.example.video_meeting_app.auth.security.config;

import com.example.video_meeting_app.auth.security.jwt.JwtTokenFilter;
import com.example.video_meeting_app.auth.security.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig{
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsService manager;
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(
                                    "/users/signin",
                                    "/users/signup"
                            )
                            .anonymous();
                    auth.requestMatchers(
                                    "/users/details",
                                    "/users/profile",
                                    "/users/get-user-info"
                            )
                            .authenticated();
                    auth.requestMatchers(
                                    "/admin/**"
                            )
                            .hasRole("ADMIN");
                    auth.requestMatchers("/error", "/static/**", "/views/**", "/")
                            .permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(
                        new JwtTokenFilter(
                                jwtTokenUtils,
                                manager
                        ),
                        AuthorizationFilter.class
                );


        return http.build();
    }
}
