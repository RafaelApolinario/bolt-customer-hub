package com.bolt.customer.application.event;

import java.time.Instant;

public record CustomerRegisteredInMgEvent(
		String topic,
		String customerId,
		String document,
		Instant occurredAt) {

	public static CustomerRegisteredInMgEvent of(String customerId, String document, Instant occurredAt) {
		return new CustomerRegisteredInMgEvent("analise_cliente_mg", customerId, document, occurredAt);
	}
}
