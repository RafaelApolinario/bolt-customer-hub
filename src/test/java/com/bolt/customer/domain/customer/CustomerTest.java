package com.bolt.customer.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.bolt.customer.domain.exception.BusinessException;

class CustomerTest {

	private static final Clock CREATED_AT = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);
	private static final Clock UPDATED_AT = Clock.fixed(Instant.parse("2026-05-20T11:00:00Z"), ZoneOffset.UTC);

	@Test
	void shouldCreateActiveCustomer() {
		Customer customer = Customer.create(" Maria Silva ", Document.of("123.456.789-01"), List.of(consumerUnit()), CREATED_AT);

		assertThat(customer.getId()).isNotNull();
		assertThat(customer.getName()).isEqualTo("Maria Silva");
		assertThat(customer.getDocument().value()).isEqualTo("12345678901");
		assertThat(customer.isActive()).isTrue();
		assertThat(customer.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
		assertThat(customer.getCreatedAt()).isEqualTo(Instant.parse("2026-05-20T10:00:00Z"));
		assertThat(customer.getUpdatedAt()).isEqualTo(Instant.parse("2026-05-20T10:00:00Z"));
	}

	@Test
	void shouldNotCreateCustomerWithoutName() {
		assertThatThrownBy(() -> Customer.create(" ", Document.of("12345678901"), List.of(consumerUnit()), CREATED_AT))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Customer name is required");
	}

	@Test
	void shouldNotCreateCustomerWithoutDocument() {
		assertThatThrownBy(() -> Customer.create("Maria Silva", null, List.of(consumerUnit()), CREATED_AT))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Customer document is required");
	}

	@Test
	void shouldNotCreateCustomerWithoutConsumerUnit() {
		assertThatThrownBy(() -> Customer.create("Maria Silva", Document.of("12345678901"), List.of(), CREATED_AT))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Customer must have at least one consumer unit");
	}

	@Test
	void shouldUpdateCustomerData() {
		Customer customer = Customer.create("Maria Silva", Document.of("12345678901"), List.of(consumerUnit()), CREATED_AT);
		ConsumerUnit newUnit = new ConsumerUnit("UC-200", new Address("30140071", "Rua B", "Centro", "Belo Horizonte", "MG"));

		customer.update("Maria Souza", Document.of("98765432100"), List.of(newUnit), UPDATED_AT);

		assertThat(customer.getName()).isEqualTo("Maria Souza");
		assertThat(customer.getDocument().value()).isEqualTo("98765432100");
		assertThat(customer.getConsumerUnits()).containsExactly(newUnit);
		assertThat(customer.getUpdatedAt()).isEqualTo(Instant.parse("2026-05-20T11:00:00Z"));
	}

	@Test
	void shouldLogicallyDeleteCustomer() {
		Customer customer = Customer.create("Maria Silva", Document.of("12345678901"), List.of(consumerUnit()), CREATED_AT);

		customer.deactivate(UPDATED_AT);

		assertThat(customer.isActive()).isFalse();
		assertThat(customer.getStatus()).isEqualTo(CustomerStatus.INACTIVE);
		assertThat(customer.getUpdatedAt()).isEqualTo(Instant.parse("2026-05-20T11:00:00Z"));
	}

	private static ConsumerUnit consumerUnit() {
		return new ConsumerUnit("UC-100", new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"));
	}
}
