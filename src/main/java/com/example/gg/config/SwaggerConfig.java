package com.example.gg.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI1(){
        Info info = new Info()
                .title("Lolonoa 프로젝트")
                .version("v3")
                .description("목표: 3달 안에 Lolonoa 프로젝트 완성시키기 2023.07.24~");
        return new OpenAPI()
                .components(new Components())
                .info(info);
    }

    @Bean
    public GroupedOpenApi group1(){
        return GroupedOpenApi.builder()
                .group("유저")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group2(){
        return GroupedOpenApi.builder()
                .group("어드민")
                .pathsToMatch("/admin/**")
                .build();
    }
}
