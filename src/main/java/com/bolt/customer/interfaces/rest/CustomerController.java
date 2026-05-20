package com.bolt.customer.interfaces.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolt.customer.application.usecase.CreateCustomerUseCase;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.interfaces.mapper.CustomerRestMapper;
import com.bolt.customer.interfaces.rest.request.CreateCustomerRequest;
import com.bolt.customer.interfaces.rest.response.CustomerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CreateCustomerUseCase createCustomerUseCase;
	private final CustomerRestMapper mapper;

	public CustomerController(CreateCustomerUseCase createCustomerUseCase, CustomerRestMapper mapper) {
		this.createCustomerUseCase = createCustomerUseCase;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
		Customer customer = createCustomerUseCase.execute(mapper.toCommand(request));
		return ResponseEntity
				.created(URI.create("/api/customers/" + customer.getId()))
				.body(mapper.toResponse(customer));
	}
}
