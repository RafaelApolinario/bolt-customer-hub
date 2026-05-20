package com.bolt.customer.infrastructure.viacep;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.bolt.customer.application.gateway.AddressGateway;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.exception.BusinessException;

@Component
public class ViaCepAddressGateway implements AddressGateway {

	private final ViaCepClient client;

	public ViaCepAddressGateway(ViaCepClient client) {
		this.client = client;
	}

	@Override
	public Address findByZipCode(String zipCode) {
		String normalizedZipCode = normalizeZipCode(zipCode);

		ViaCepAddressResponse response;
		try {
			response = client.findByZipCode(normalizedZipCode);
		} catch (RestClientException exception) {
			throw new BusinessException("address.zip-code.lookup-failed", "Could not fetch address from ViaCEP");
		}

		if (response == null || response.hasError()) {
			throw new BusinessException("address.zip-code.not-found", "Zip code not found in ViaCEP");
		}

		return new Address(
				normalizedZipCode,
				response.street(),
				response.neighborhood(),
				response.city(),
				response.state());
	}

	private static String normalizeZipCode(String value) {
		if (value == null || value.isBlank()) {
			throw new BusinessException("address.zip-code.required", "Zip code is required");
		}

		String normalized = value.replaceAll("\\D", "");
		if (normalized.length() != 8) {
			throw new BusinessException("address.zip-code.invalid", "Zip code must have 8 digits");
		}
		return normalized;
	}
}
