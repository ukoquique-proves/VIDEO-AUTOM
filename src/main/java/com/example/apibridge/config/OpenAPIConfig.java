package com.example.apibridge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI apiBridgeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Logistics Automation Hub")
                        .description("AI-driven document extraction pipeline for logistics automation — sends structured JSON results to Slack and Email.")
                        .version("1.0"));
    }
}
