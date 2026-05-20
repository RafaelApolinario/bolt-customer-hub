package com.bolt.customer.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bolt.customer.application.command.ConsumerUnitCommand;
import com.bolt.customer.application.command.UpdateCustomerCommand;
import com.bolt.customer.application.gateway.AddressGateway;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerDomainService;
import com.bolt.customer.domain.customer.CustomerId;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.domain.exception.ConflictException;
import com.bolt.customer.domain.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class UpdateCustomerUseCaseTest {

	private static final Clock CREATED_AT = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);
	private static final Clock UPDATED_AT = Clock.fixed(Instant.parse("2026-05-20T11:00:00Z"), ZoneOffset.UTC);

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AddressGateway addressGateway;

	private UpdateCustomerUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new UpdateCustomerUseCase(customerRepository, addressGateway, new CustomerDomainService(), UPDATED_AT);
	}

	@Test
	void shouldUpdateCustomerWhenDataIsValid() {
		Customer customer = customer();
		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("MG"));
		when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Customer updated = useCase.execute(command(customer.getId(), "98765432100", "UC-200", "30140071"));

		assertThat(updated.getName()).isEqualTo("Maria Souza");
		assertThat(updated.getDocument()).isEqualTo(Document.of("98765432100"));
		assertThat(updated.getConsumerUnits()).extracting(ConsumerUnit::number).containsExactly("UC-200");
		assertThat(updated.getUpdatedAt()).isEqualTo(Instant.parse("2026-05-20T11:00:00Z"));
	}

	@Test
	void shouldNotUpdateUnknownCustomer() {
		CustomerId id = CustomerId.generate();
		when(customerRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> useCase.execute(command(id, "12345678901", "UC-100", "30140071")))
				.isInstanceOf(NotFoundException.class)
				.hasMessage("Customer not found");
	}

	@Test
	void shouldNotUpdateCustomerWithDuplicatedDocument() {
		Customer customer = customer();
		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
		when(customerRepository.existsByDocumentAndIdNot(Document.of("98765432100"), customer.getId())).thenReturn(true);

		assertThatThrownBy(() -> useCase.execute(command(customer.getId(), "98765432100", "UC-100", "30140071")))
				.isInstanceOf(ConflictException.class)
				.hasMessage("Document already exists");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	@Test
	void shouldNotUpdateCustomerWithConsumerUnitFromAnotherCustomer() {
		Customer customer = customer();
		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));
		when(addressGateway.findByZipCode("30140071")).thenReturn(address("MG"));
		when(customerRepository.existsByConsumerUnitNumberAndCustomerIdNot("UC-999", customer.getId())).thenReturn(true);

		assertThatThrownBy(() -> useCase.execute(command(customer.getId(), "12345678901", "UC-999", "30140071")))
				.isInstanceOf(ConflictException.class)
				.hasMessage("Consumer unit already belongs to another customer");

		verify(customerRepository, never()).save(any(Customer.class));
	}

	private static UpdateCustomerCommand command(CustomerId id, String document, String consumerUnitNumber, String zipCode) {
		return new UpdateCustomerCommand(
				id,
				"Maria Souza",
				document,
				List.of(new ConsumerUnitCommand(consumerUnitNumber, zipCode)));
	}

	private static Customer customer() {
		return Customer.create(
				"Maria Silva",
				Document.of("12345678901"),
				List.of(new ConsumerUnit("UC-100", address("MG"))),
				CREATED_AT);
	}

	private static Address address(String state) {
		return new Address("30140071", "Rua A", "Centro", "Belo Horizonte", state);
	}
}
