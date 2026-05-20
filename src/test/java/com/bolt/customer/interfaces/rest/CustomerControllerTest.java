package com.bolt.customer.interfaces.rest;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.bolt.customer.application.usecase.CreateCustomerUseCase;
import com.bolt.customer.application.usecase.DeleteCustomerUseCase;
import com.bolt.customer.application.usecase.UpdateCustomerUseCase;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.Document;
import com.bolt.customer.interfaces.mapper.CustomerRestMapper;
import com.bolt.customer.interfaces.rest.handler.RestExceptionHandler;

@WebMvcTest(CustomerController.class)
@Import({ CustomerRestMapper.class, RestExceptionHandler.class })
class CustomerControllerTest {

	private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-05-20T10:00:00Z"), ZoneOffset.UTC);

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CreateCustomerUseCase createCustomerUseCase;

	@MockitoBean
	private UpdateCustomerUseCase updateCustomerUseCase;

	@MockitoBean
	private DeleteCustomerUseCase deleteCustomerUseCase;

	@Test
	void shouldCreateCustomer() throws Exception {
		Customer customer = Customer.create(
				"Maria Silva",
				Document.of("12345678901"),
				List.of(new ConsumerUnit("UC-100", new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"))),
				CLOCK);
		when(createCustomerUseCase.execute(any())).thenReturn(customer);

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Maria Silva",
								  "document": "12345678901",
								  "consumerUnits": [
								    {
								      "number": "UC-100",
								      "zipCode": "30140071"
								    }
								  ]
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(header().string("Location", "/api/customers/" + customer.getId()))
				.andExpect(jsonPath("$.id").value(customer.getId().toString()))
				.andExpect(jsonPath("$.name").value("Maria Silva"))
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.consumerUnits[0].state").value("MG"));
	}

	@Test
	void shouldReturnBadRequestWhenPayloadIsInvalid() throws Exception {
		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "",
								  "document": "",
								  "consumerUnits": []
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.timestamp", notNullValue()))
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.path").value("/api/customers"));
	}

	@Test
	void shouldUpdateCustomer() throws Exception {
		Customer customer = Customer.create(
				"Maria Souza",
				Document.of("12345678901"),
				List.of(new ConsumerUnit("UC-100", new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"))),
				CLOCK);
		when(updateCustomerUseCase.execute(any())).thenReturn(customer);

		mockMvc.perform(put("/api/customers/{id}", customer.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Maria Souza",
								  "document": "12345678901",
								  "consumerUnits": [
								    {
								      "number": "UC-100",
								      "zipCode": "30140071"
								    }
								  ]
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(customer.getId().toString()))
				.andExpect(jsonPath("$.name").value("Maria Souza"));
	}

	@Test
	void shouldDeleteCustomer() throws Exception {
		Customer customer = Customer.create(
				"Maria Silva",
				Document.of("12345678901"),
				List.of(new ConsumerUnit("UC-100", new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"))),
				CLOCK);

		mockMvc.perform(delete("/api/customers/{id}", customer.getId()))
				.andExpect(status().isNoContent());
	}
}
