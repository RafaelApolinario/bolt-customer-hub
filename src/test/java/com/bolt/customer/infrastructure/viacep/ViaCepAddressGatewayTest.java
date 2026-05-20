package com.bolt.customer.infrastructure.viacep;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class ViaCepAddressGatewayTest {

	@Mock
	private ViaCepClient client;

	@InjectMocks
	private ViaCepAddressGateway gateway;

	@Test
	void shouldReturnAddressWhenZipCodeExists() {
		when(client.findByZipCode("30140071"))
				.thenReturn(new ViaCepAddressResponse("30140-071", "Rua dos Timbiras", "Funcionários", "Belo Horizonte", "MG", false));

		Address address = gateway.findByZipCode("30140-071");

		assertThat(address.zipCode()).isEqualTo("30140071");
		assertThat(address.street()).isEqualTo("Rua dos Timbiras");
		assertThat(address.neighborhood()).isEqualTo("Funcionários");
		assertThat(address.city()).isEqualTo("Belo Horizonte");
		assertThat(address.state()).isEqualTo("MG");
		verify(client).findByZipCode("30140071");
	}

	@Test
	void shouldThrowBusinessExceptionWhenZipCodeDoesNotExist() {
		when(client.findByZipCode("99999999"))
				.thenReturn(new ViaCepAddressResponse(null, null, null, null, null, true));

		assertThatThrownBy(() -> gateway.findByZipCode("99999-999"))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Zip code not found in ViaCEP");
	}

	@Test
	void shouldThrowBusinessExceptionWhenZipCodeIsInvalid() {
		assertThatThrownBy(() -> gateway.findByZipCode("123"))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Zip code must have 8 digits");

		verify(client, never()).findByZipCode("123");
	}

	@Test
	void shouldWrapViaCepCommunicationFailures() {
		when(client.findByZipCode("30140071")).thenThrow(new RestClientException("timeout"));

		assertThatThrownBy(() -> gateway.findByZipCode("30140071"))
				.isInstanceOf(BusinessException.class)
				.hasMessage("Could not fetch address from ViaCEP");
	}
}
