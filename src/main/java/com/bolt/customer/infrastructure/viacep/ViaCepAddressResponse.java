package com.bolt.customer.infrastructure.viacep;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ViaCepAddressResponse(
		String cep,
		@JsonProperty("logradouro") String street,
		@JsonProperty("bairro") String neighborhood,
		@JsonProperty("localidade") String city,
		@JsonProperty("uf") String state,
		@JsonProperty("erro") Boolean error) {

	public boolean hasError() {
		return Boolean.TRUE.equals(error);
	}
}
