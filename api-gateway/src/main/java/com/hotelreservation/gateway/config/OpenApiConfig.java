package com.hotelreservation.gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration
 *
 * Configuration for OpenAPI 3 documentation.
 *
 * @author Hotel Reservation System
 * @version 1.0.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * OpenAPI Configuration Bean
     *
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI apiGatewayOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Development server");

        Server prodServer = new Server();
        prodServer.setUrl("http://api-gateway:8080");
        prodServer.setDescription("Production server");

        Contact contact = new Contact();
        contact.setName("Hotel Reservation System");
        contact.setEmail("support@hotelreservation.com");
        contact.setUrl("https://www.hotelreservation.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("API Gateway")
                .version("1.0.0")
                .description("This is the API Gateway for the Hotel Reservation System. It provides unified access to all microservices.")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
} 