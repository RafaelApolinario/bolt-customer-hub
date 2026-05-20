package com.bolt.customer.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI customerApiOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Bolt Customer Hub API")
						.version("v1")
						.description("Customer Management API for the Bolt technical challenge."))
				.addServersItem(new Server().url("http://localhost:8082").description("Local development"));
	}
}
