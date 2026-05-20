package com.bolt.customer.interfaces.rest.handler;

import java.time.Instant;

public record ErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path) {
}
