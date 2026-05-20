package com.bolt.customer.interfaces.rest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bolt.customer.application.gateway.AddressGateway;
import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.infrastructure.persistence.SpringDataCustomerRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private SpringDataCustomerRepository repository;

	@MockitoBean
	private AddressGateway addressGateway;

	@BeforeEach
	void setUp() {
		repository.deleteAll();
	}

	@Test
	void postCustomersShouldReturnCreated() throws Exception {
		when(addressGateway.findByZipCode("30140071"))
				.thenReturn(new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"));

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload("Maria Silva", "12345678901", "UC-100", "30140071")))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.active").value(true))
				.andExpect(jsonPath("$.consumerUnits[0].state").value("MG"));
	}

	@Test
	void postCustomersShouldReturnConflictForDuplicatedDocument() throws Exception {
		when(addressGateway.findByZipCode("30140071"))
				.thenReturn(new Address("30140071", "Rua A", "Centro", "Belo Horizonte", "MG"));

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload("Maria Silva", "12345678901", "UC-100", "30140071")))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload("João Souza", "12345678901", "UC-200", "30140071")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Document already exists"));
	}

	@Test
	void postCustomersShouldReturnUnprocessableEntityForBlockedState() throws Exception {
		when(addressGateway.findByZipCode("01001000"))
				.thenReturn(new Address("01001000", "Praça da Sé", "Sé", "São Paulo", "SP"));

		mockMvc.perform(post("/api/customers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(payload("Maria Silva", "12345678901", "UC-100", "01001000")))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message").value("Consumer units in SP, RS or PR are not allowed"));
	}

	private static String payload(String name, String document, String unitNumber, String zipCode) {
		return """
				{
				  "name": "%s",
				  "document": "%s",
				  "consumerUnits": [
				    {
				      "number": "%s",
				      "zipCode": "%s"
				    }
				  ]
				}
				""".formatted(name, document, unitNumber, zipCode);
	}
}
