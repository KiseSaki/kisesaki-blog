package com.kisesaki.blog.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

/**
 * OpenAPI配置类 (Swagger文档)
 * 
 * @author KiseSaki
 * @since 2025-08-07
 */
@Configuration
public class OpenApiConfig {

    /**
     * OpenAPI配置
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(apiInfo())
                .servers(List.of(new Server().url("http://localhost:8080").description("开发环境"),
                        new Server().url("https://api.kisesaki.com").description("生产环境")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(
                        "Bearer Authentication",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer")
                                .bearerFormat("JWT")
                                .description("请输入JWT Token，格式：Bearer {token}")));
    }

    /**
     * API信息
     */
    private Info apiInfo() {
        return new Info().title("KiseSaki Blog API").description("KiseSaki 个人博客系统 RESTful API 文档")
                .version("1.0.0")
                .contact(new Contact().name("KiseSaki").email("kisesaki@example.com")
                        .url("https://github.com/KiseSaki"))
                .license(new License().name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }
}
