package com.pcp.funeralsvc.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfiguration {

    private static final String API_NAME = "Funeral";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "PetCare Funeral API 문서 입니다.";

    @Bean
    public OpenAPI OpenAPIConfig() {
        return new OpenAPI()
                .info(new Info().title(API_NAME).description(API_DESCRIPTION).version(API_VERSION));
    }
}
