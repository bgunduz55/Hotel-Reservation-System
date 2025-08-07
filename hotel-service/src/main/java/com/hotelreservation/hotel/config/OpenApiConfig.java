package com.hotelreservation.hotel.config;

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
    public OpenAPI hotelServiceOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8081");
        devServer.setDescription("Development server");

        Server prodServer = new Server();
        prodServer.setUrl("http://hotel-service:8081");
        prodServer.setDescription("Production server");

        Contact contact = new Contact();
        contact.setName("Hotel Reservation System");
        contact.setEmail("support@hotelreservation.com");
        contact.setUrl("https://www.hotelreservation.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Hotel Service API")
                .version("1.0.0")
                .description("This API provides endpoints for managing hotels and rooms in the Hotel Reservation System.")
                .contact(contact)
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
} 