package com.bolt.customer.application.command;

import java.util.List;

public record CreateCustomerCommand(
		String name,
		String document,
		List<ConsumerUnitCommand> consumerUnits) {
}
