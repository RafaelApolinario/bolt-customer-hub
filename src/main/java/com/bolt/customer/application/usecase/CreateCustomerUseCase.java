package com.bolt.customer.application.usecase;

import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolt.customer.application.command.ConsumerUnitCommand;
import com.bolt.customer.application.command.CreateCustomerCommand;
import com.bolt.customer.application.gateway.AddressGateway;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerDomainService;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.domain.exception.ConflictException;

@Service
public class CreateCustomerUseCase {

	private final CustomerRepository customerRepository;
	private final AddressGateway addressGateway;
	private final CustomerDomainService customerDomainService;
	private final Clock clock;

	public CreateCustomerUseCase(
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
	public Customer execute(CreateCustomerCommand command) {
		Document document = Document.of(command.document());
		ensureDocumentIsAvailable(document);

		List<ConsumerUnit> consumerUnits = command.consumerUnits().stream()
				.map(this::createConsumerUnit)
				.toList();

		ensureNoDuplicatedConsumerUnitsInRequest(consumerUnits);
		customerDomainService.ensureConsumerUnitsAreAllowed(consumerUnits);

		Customer customer = Customer.create(command.name(), document, consumerUnits, clock);
		return customerRepository.save(customer);
	}

	private ConsumerUnit createConsumerUnit(ConsumerUnitCommand command) {
		Address address = addressGateway.findByZipCode(command.zipCode());
		ConsumerUnit consumerUnit = new ConsumerUnit(command.number(), address);
		ensureConsumerUnitIsAvailable(consumerUnit.number());
		return consumerUnit;
	}

	private void ensureDocumentIsAvailable(Document document) {
		if (customerRepository.existsByDocument(document)) {
			throw new ConflictException("customer.document.duplicated", "Document already exists");
		}
	}

	private void ensureConsumerUnitIsAvailable(String number) {
		if (customerRepository.existsByConsumerUnitNumber(number)) {
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
