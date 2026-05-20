package com.bolt.customer.application.usecase;

import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolt.customer.application.command.ConsumerUnitCommand;
import com.bolt.customer.application.command.UpdateCustomerCommand;
import com.bolt.customer.application.gateway.AddressGateway;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerDomainService;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.domain.exception.ConflictException;
import com.bolt.customer.domain.exception.NotFoundException;

@Service
public class UpdateCustomerUseCase {

	private final CustomerRepository customerRepository;
	private final AddressGateway addressGateway;
	private final CustomerDomainService customerDomainService;
	private final Clock clock;

	public UpdateCustomerUseCase(
			CustomerRepository customerRepository,
			AddressGateway addressGateway,
			CustomerDomainService customerDomainService,
			Clock clock) {
		this.customerRepository = customerRepository;
		this.addressGateway = addressGateway;
		this.customerDomainService = customerDomainService;
		this.clock = clock;
	}

	@Transactional
	public Customer execute(UpdateCustomerCommand command) {
		Customer customer = customerRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("customer", "Customer not found"));

		Document document = Document.of(command.document());
		ensureDocumentIsAvailable(document, command);

		List<ConsumerUnit> consumerUnits = command.consumerUnits().stream()
				.map(unit -> createConsumerUnit(unit, command))
				.toList();

		ensureNoDuplicatedConsumerUnitsInRequest(consumerUnits);
		customerDomainService.ensureConsumerUnitsAreAllowed(consumerUnits);

		customer.update(command.name(), document, consumerUnits, clock);
		return customerRepository.save(customer);
	}

	private ConsumerUnit createConsumerUnit(ConsumerUnitCommand command, UpdateCustomerCommand updateCommand) {
		Address address = addressGateway.findByZipCode(command.zipCode());
		ConsumerUnit consumerUnit = new ConsumerUnit(command.number(), address);
		ensureConsumerUnitIsAvailable(consumerUnit.number(), updateCommand);
		return consumerUnit;
	}

	private void ensureDocumentIsAvailable(Document document, UpdateCustomerCommand command) {
		if (customerRepository.existsByDocumentAndIdNot(document, command.id())) {
			throw new ConflictException("customer.document.duplicated", "Document already exists");
		}
	}

	private void ensureConsumerUnitIsAvailable(String number, UpdateCustomerCommand command) {
		if (customerRepository.existsByConsumerUnitNumberAndCustomerIdNot(number, command.id())) {
			throw new ConflictException("consumer-unit.number.duplicated", "Consumer unit already belongs to another customer");
		}
	}

	private void ensureNoDuplicatedConsumerUnitsInRequest(List<ConsumerUnit> consumerUnits) {
		Set<String> numbers = new HashSet<>();
		for (ConsumerUnit consumerUnit : consumerUnits) {
			if (!numbers.add(consumerUnit.number())) {
				throw new ConflictException("consumer-unit.number.duplicated", "Consumer unit appears more than once in request");
			}
		}
	}
}
