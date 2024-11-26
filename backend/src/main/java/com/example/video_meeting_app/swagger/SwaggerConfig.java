package com.example.video_meeting_app.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "My API",
                description = "API for managing video meeting application",
                version = "v1"
        )
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch("/api/user/**")
                .build();
    } @Bean
    public GroupedOpenApi roomApi() {
        return GroupedOpenApi.builder()
                .group("Room API")
                .pathsToMatch("/api/rooms/**")
                .build();
    }

}
