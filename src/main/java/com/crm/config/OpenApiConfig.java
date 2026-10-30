package com.crm.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CRM System API")
                        .version("1.0.0")
                        .description("Modular CRM System for B2B/B2C business process automation")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@crm.com")));
    }
}
