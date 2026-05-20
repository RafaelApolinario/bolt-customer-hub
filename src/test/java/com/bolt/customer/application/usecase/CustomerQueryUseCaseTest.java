package com.bolt.customer.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bolt.customer.application.query.GetCustomerByIdQuery;
import com.bolt.customer.application.query.ListCustomersQuery;
import com.bolt.customer.application.query.ListLatestCustomersQuery;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerId;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.domain.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomerQueryUseCaseTest {

	private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);

	@Mock
	private CustomerRepository customerRepository;

	@Test
	void shouldGetCustomerById() {
		Customer customer = customer();
		when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

		Customer found = new GetCustomerByIdUseCase(customerRepository).execute(new GetCustomerByIdQuery(customer.getId()));

		assertThat(found).isEqualTo(customer);
	}

	@Test
	void shouldReturnNotFoundWhenCustomerDoesNotExist() {
		CustomerId id = CustomerId.generate();
		when(customerRepository.findById(id)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> new GetCustomerByIdUseCase(customerRepository).execute(new GetCustomerByIdQuery(id)))
				.isInstanceOf(NotFoundException.class)
				.hasMessage("Customer not found");
	}

	@Test
	void shouldListActiveCustomers() {
		Customer customer = customer();
		when(customerRepository.findAllActive()).thenReturn(List.of(customer));

		List<Customer> customers = new ListCustomersUseCase(customerRepository).execute(new ListCustomersQuery());

		assertThat(customers).containsExactly(customer);
	}

	@Test
	void shouldListLatestCustomersWithMaxLimit() {
		Customer customer = customer();
		when(customerRepository.findLatestActive(20)).thenReturn(List.of(customer));

		List<Customer> customers = new ListLatestCustomersUseCase(customerRepository)
				.execute(new ListLatestCustomersQuery(50));

		assertThat(customers).containsExactly(customer);
	}

	private static Customer customer() {
		return Customer.create(
				"Maria Silva",
				Document.of("12345678901"),
				List.of(new ConsumerUnit("UC-100", new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"))),
				CLOCK);
	}
}
