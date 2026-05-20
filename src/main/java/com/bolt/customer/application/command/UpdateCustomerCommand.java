package com.bolt.customer.application.command;

import java.util.List;

import com.bolt.customer.domain.customer.CustomerId;

public record UpdateCustomerCommand(
		CustomerId id,
		String name,
		String document,
		List<ConsumerUnitCommand> consumerUnits) {
}
