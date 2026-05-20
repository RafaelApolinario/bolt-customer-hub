package com.bolt.customer.domain.customer;

import com.bolt.customer.domain.exception.BusinessException;

public record Document(String value) {

	public Document {
		value = normalize(value);
	}

	public static Document of(String value) {
		return new Document(value);
	}

	private static String normalize(String value) {
		if (value == null || value.isBlank()) {
			throw new BusinessException("document.required", "Document is required");
		}

		String normalized = value.replaceAll("\\D", "");
		if (normalized.isBlank()) {
			throw new BusinessException("document.invalid", "Document must contain digits");
		}
		return normalized;
	}
}
