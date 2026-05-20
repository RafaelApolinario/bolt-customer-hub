package com.bolt.customer.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.bolt.customer.domain.exception.BusinessException;

class CustomerDomainServiceTest {

	private final CustomerDomainService service = new CustomerDomainService();

	@Test
	void shouldBlockConsumerUnitsInRestrictedStates() {
		List<ConsumerUnit> units = List.of(consumerUnit("SP"));

		assertThatThrownBy(() -> service.ensureConsumerUnitsAreAllowed(units))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Consumer units in SP, RS or PR are not allowed");
	}

	@Test
	void shouldIdentifyCustomerThatRequiresMgAnalysis() {
		Customer customer = Customer.create("Maria Silva", Document.of("12345678901"), List.of(consumerUnit("MG")));

		assertThat(service.requiresMgAnalysis(customer)).isTrue();
	}

	@Test
	void shouldIdentifyCustomerThatDoesNotRequireMgAnalysis() {
		Customer customer = Customer.create("Maria Silva", Document.of("12345678901"), List.of(consumerUnit("RJ")));

		assertThat(service.requiresMgAnalysis(customer)).isFalse();
	}

	private static ConsumerUnit consumerUnit(String state) {
		return new ConsumerUnit("UC-" + state, new Address("30140071", "Rua A", "Centro", "Cidade", state));
	}
}
