package com.bolt.customer.infrastructure.config;

import java.time.Clock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bolt.customer.domain.customer.CustomerDomainService;

@Configuration
public class ApplicationConfig {

	@Bean
	Clock clock() {
		return Clock.systemUTC();
	}

	@Bean
	CustomerDomainService customerDomainService() {
		return new CustomerDomainService();
	}
}
