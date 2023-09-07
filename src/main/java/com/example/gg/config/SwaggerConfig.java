package com.example.gg.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
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
//                .addServersItem(new Server().url("/"))
                .addServersItem(new Server().url("https://api.lolonoa.site/"))
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
                .group("api")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group3(){
        return GroupedOpenApi.builder()
                .group("어드민")
                .pathsToMatch("/admin/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group4(){
        return GroupedOpenApi.builder()
                .group("이메일")
                .pathsToMatch("/email/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group5(){
        return GroupedOpenApi.builder()
                .group("라이엇")
                .pathsToMatch("/riot/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group6(){
        return GroupedOpenApi.builder()
                .group("롤문철")
                .pathsToMatch("/judgement/**")
                .build();
    }

    @Bean
    public GroupedOpenApi group7(){
        return GroupedOpenApi.builder()
                .group("피드백")
                .pathsToMatch("/feedback/**")
                .build();
    }
}
