package com.example.video_meeting_app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {
    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final TokenOAuth2Handler oAuth2Handler;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth->{
                    auth.requestMatchers(HttpMethod.POST, "/users").permitAll();
                    auth.requestMatchers("/token/**", "/static/**",
                            "/ws", "/topic", "/app", "/oauth2/**").permitAll();
//                    auth.requestMatchers("/users/**").authenticated();
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(
                        new TokenFilterHandler(tokenUtils, userService),
                        AuthorizationFilter.class
                )
                .oauth2Login(oauth2->oauth2
                        .userInfoEndpoint(userInfo-> userInfo
                                .userService(oAuth2UserServiceSelector()))
                        .successHandler(oAuth2Handler)
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserServiceSelector(){
        return userRequest -> {
            String registerId = userRequest.getClientRegistration().getRegistrationId();
            if ("naver".equals(registerId)){
                return naverService.loadUser(userRequest);
            }
            else if ("kakao".equals(registerId)){
                return kakaoService.loadUser(userRequest);
            }
            else {
                throw new OAuth2AuthenticationException("Unsupported provider"+ registerId);
            }
        };
    }
}
