package com.bolt.customer.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.Document;

@DataJpaTest
@Import(JpaCustomerRepository.class)
class JpaCustomerRepositoryTest {

	private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);

	@Autowired
	private CustomerRepository repository;

	@Test
	void shouldSaveAndFindCustomer() {
		Customer customer = customer("12345678901", "UC-100");

		Customer saved = repository.save(customer);

		assertThat(repository.findById(saved.getId())).isPresent();
		assertThat(repository.existsByDocument(Document.of("12345678901"))).isTrue();
		assertThat(repository.existsByConsumerUnitNumber("UC-100")).isTrue();
	}

	@Test
	void shouldListOnlyActiveCustomers() {
		Customer active = repository.save(customer("12345678901", "UC-100"));
		Customer inactive = customer("98765432100", "UC-200");
		inactive.deactivate(CLOCK);
		repository.save(inactive);

		assertThat(repository.findAllActive())
				.extracting(Customer::getId)
				.containsExactly(active.getId());
	}

	private static Customer customer(String document, String consumerUnitNumber) {
		return Customer.create(
				"Maria Silva",
				Document.of(document),
				List.of(new ConsumerUnit(consumerUnitNumber, new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"))),
				CLOCK);
	}
}
