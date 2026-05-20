package com.bolt.customer.interfaces.rest.response;

import java.time.Instant;
import java.util.List;

public record CustomerResponse(
		String id,
		String name,
		String document,
		boolean active,
		List<ConsumerUnitResponse> consumerUnits,
		Instant createdAt,
		Instant updatedAt) {
}
