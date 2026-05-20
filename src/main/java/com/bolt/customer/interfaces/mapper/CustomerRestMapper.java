package com.bolt.customer.interfaces.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bolt.customer.application.command.ConsumerUnitCommand;
import com.bolt.customer.application.command.CreateCustomerCommand;
import com.bolt.customer.application.command.UpdateCustomerCommand;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerId;
import com.bolt.customer.interfaces.rest.request.ConsumerUnitRequest;
import com.bolt.customer.interfaces.rest.request.CreateCustomerRequest;
import com.bolt.customer.interfaces.rest.request.UpdateCustomerRequest;
import com.bolt.customer.interfaces.rest.response.ConsumerUnitResponse;
import com.bolt.customer.interfaces.rest.response.CustomerResponse;

@Component
public class CustomerRestMapper {

	public CreateCustomerCommand toCommand(CreateCustomerRequest request) {
		return new CreateCustomerCommand(
				request.name(),
				request.document(),
				request.consumerUnits().stream()
						.map(this::toCommand)
						.toList());
	}

	public UpdateCustomerCommand toCommand(String id, UpdateCustomerRequest request) {
		return new UpdateCustomerCommand(
				CustomerId.from(id),
				request.name(),
				request.document(),
				request.consumerUnits().stream()
						.map(this::toCommand)
						.toList());
	}

	public CustomerResponse toResponse(Customer customer) {
		return new CustomerResponse(
				customer.getId().toString(),
				customer.getName(),
				customer.getDocument().value(),
				customer.isActive(),
				toConsumerUnitResponses(customer.getConsumerUnits()),
				customer.getCreatedAt(),
				customer.getUpdatedAt());
	}

	private ConsumerUnitCommand toCommand(ConsumerUnitRequest request) {
		return new ConsumerUnitCommand(request.number(), request.zipCode());
	}

	private List<ConsumerUnitResponse> toConsumerUnitResponses(List<ConsumerUnit> consumerUnits) {
		return consumerUnits.stream()
				.map(unit -> new ConsumerUnitResponse(
						unit.number(),
						unit.address().zipCode(),
						unit.address().street(),
						unit.address().neighborhood(),
						unit.address().city(),
						unit.address().state()))
				.toList();
	}
}
