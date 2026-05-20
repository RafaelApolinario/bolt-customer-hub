package com.bolt.customer.interfaces.rest.response;

public record ConsumerUnitResponse(
		String number,
		String zipCode,
		String street,
		String neighborhood,
		String city,
		String state) {
}
