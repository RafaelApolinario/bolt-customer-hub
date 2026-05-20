package com.bolt.customer.infrastructure.viacep;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient {

	private final RestClient restClient;

	public ViaCepClient(RestClient.Builder builder, @Value("${viacep.base-url}") String baseUrl) {
		this.restClient = builder.baseUrl(baseUrl).build();
	}

	public ViaCepAddressResponse findByZipCode(String zipCode) {
		return restClient.get()
				.uri("/{zipCode}/json/", zipCode)
				.retrieve()
				.body(ViaCepAddressResponse.class);
	}
}
