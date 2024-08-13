package com.parking.profile_service.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${open.api.title}") String title,
            @Value("${open.api.version}") String version,
            @Value("${open.api.serverUrl}") String serverUrl,
            @Value("${open.api.serverName}") String serverName
    ) {

        License license = new License()
                .name("API License")
                .url("License");

        Info info = new Info()
                .title(title)
                .version(version)
                .description("description")
                .license(license);

        List<Server> list = List.of(new Server()
                .url(serverUrl)
                .description(serverName));

        return new OpenAPI().info(info)
                .servers(list);
    }

    @Bean
    public GroupedOpenApi groupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("profile-service")
                .packagesToScan("com.parking.profile_service.controller")
                .build();
    }
}
