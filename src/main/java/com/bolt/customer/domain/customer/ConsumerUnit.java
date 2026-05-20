package com.bolt.customer.domain.customer;

import com.bolt.customer.domain.exception.BusinessException;

public record ConsumerUnit(String number, Address address) {

	public ConsumerUnit {
		number = normalizeNumber(number);
		if (address == null) {
			throw new BusinessException("consumer-unit.address.required", "Consumer unit address is required");
		}
	}

	public String state() {
		return address.state();
	}

	private static String normalizeNumber(String value) {
		if (value == null || value.isBlank()) {
			throw new BusinessException("consumer-unit.number.required", "Consumer unit number is required");
		}
		return value.trim();
	}
}
