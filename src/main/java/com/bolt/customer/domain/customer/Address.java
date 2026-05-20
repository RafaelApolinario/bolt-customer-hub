package com.bolt.customer.domain.customer;

import java.util.Locale;

import com.bolt.customer.domain.exception.BusinessException;

public record Address(
		String zipCode,
		String street,
		String neighborhood,
		String city,
		String state) {

	public Address {
		zipCode = normalizeZipCode(zipCode);
		street = normalizeOptional(street);
		neighborhood = normalizeOptional(neighborhood);
		city = requireNonBlank(city, "city");
		state = normalizeState(state);
	}

	private static String normalizeZipCode(String value) {
		String normalized = requireNonBlank(value, "zipCode").replaceAll("\\D", "");
		if (normalized.length() != 8) {
			throw new BusinessException("address.zip-code.invalid", "Zip code must have 8 digits");
		}
		return normalized;
	}

	private static String normalizeState(String value) {
		String normalized = requireNonBlank(value, "state").toUpperCase(Locale.ROOT);
		if (normalized.length() != 2) {
			throw new BusinessException("address.state.invalid", "State must have 2 letters");
		}
		return normalized;
	}

	private static String requireNonBlank(String value, String field) {
		if (value == null || value.isBlank()) {
			throw new BusinessException("address." + field + ".required", "Address " + field + " is required");
		}
		return value.trim();
	}

	private static String normalizeOptional(String value) {
		return value == null ? "" : value.trim();
	}
}
