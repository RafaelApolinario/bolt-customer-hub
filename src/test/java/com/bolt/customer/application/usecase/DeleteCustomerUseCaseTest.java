package com.bolt.customer.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

import com.bolt.customer.application.command.DeleteCustomerCommand;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerId;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.domain.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteCustomerUseCaseTest {

	private static final Clock CREATED_AT = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);
	private static final Clock DELETED_AT = Clock.fixed(Instant.parse("2026-05-20T11:00:00Z"), ZoneOffset.UTC);

	@Mock
	private CustomerRepository customerRepository;

	private DeleteCustomerUseCase useCase;

	@BeforeEach
	void setUp() {
		useCase = new DeleteCustomerUseCase(customerRepository, DELETED_AT);
	}

	@Test
	void shouldLogicallyDeleteCustomer() {
		Customer customer = customer();
		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

		useCase.execute(new DeleteCustomerCommand(customer.getId()));

		assertThat(customer.isActive()).isFalse();
		assertThat(customer.getUpdatedAt()).isEqualTo(Instant.parse("2026-05-20T11:00:00Z"));
		verify(customerRepository).save(customer);
	}

	@Test
	void shouldNotDeleteUnknownCustomer() {
		CustomerId id = CustomerId.generate();
		when(customerRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> useCase.execute(new DeleteCustomerCommand(id)))
				.isInstanceOf(NotFoundException.class)
				.hasMessage("Customer not found");

		verify(customerRepository, org.mockito.Mockito.never()).save(any(Customer.class));
	}

	private static Customer customer() {
		return Customer.create(
				"Maria Silva",
				Document.of("12345678901"),
				List.of(new ConsumerUnit("UC-100", new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"))),
				CREATED_AT);
	}
}
