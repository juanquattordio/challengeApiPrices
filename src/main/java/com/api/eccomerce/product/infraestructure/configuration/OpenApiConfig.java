package com.api.eccomerce.product.infraestructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@OpenAPIDefinition()
public class OpenApiConfig {
    @Bean
    public io.swagger.v3.oas.models.OpenAPI customOpenAPI() {
        return new io.swagger.v3.oas.models.OpenAPI()
                .info(
                        new Info()
                                .title("API documentation")
                                .description("Inditex eccomerce API documentation")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .name("Quattordio Juan")
                                                .email("juanquattordio@gmail.com")
                                                .url(
                                                        "https://www.linkedin.com/in/juanquattordio/")))
                .servers(
                        List.of(
                                new Server()
                                        .url("http://localhost:8080")
                                        .description("Local server")));
    }
}
