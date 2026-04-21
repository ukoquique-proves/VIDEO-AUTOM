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
                        .title("The API Bridge")
                        .description("Bridge legacy system results to modern tools (Slack, Email) with AI integration.")
                        .version("1.0"));
    }
}
