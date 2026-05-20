package com.bolt.customer.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;

public record ConsumerUnitRequest(
		@NotBlank String number,
		@NotBlank String zipCode) {
}
