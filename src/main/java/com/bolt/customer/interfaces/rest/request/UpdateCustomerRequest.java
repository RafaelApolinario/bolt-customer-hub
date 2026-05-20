package com.bolt.customer.interfaces.rest.request;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record UpdateCustomerRequest(
		@NotBlank String name,
		@NotBlank String document,
		@NotEmpty @Valid List<ConsumerUnitRequest> consumerUnits) {
}
