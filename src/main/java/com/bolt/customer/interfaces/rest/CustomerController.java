package com.bolt.customer.interfaces.rest;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolt.customer.application.usecase.CreateCustomerUseCase;
import com.bolt.customer.application.usecase.UpdateCustomerUseCase;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.interfaces.mapper.CustomerRestMapper;
import com.bolt.customer.interfaces.rest.request.CreateCustomerRequest;
import com.bolt.customer.interfaces.rest.request.UpdateCustomerRequest;
import com.bolt.customer.interfaces.rest.response.CustomerResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CreateCustomerUseCase createCustomerUseCase;
	private final UpdateCustomerUseCase updateCustomerUseCase;
	private final CustomerRestMapper mapper;

	public CustomerController(
			CreateCustomerUseCase createCustomerUseCase,
			UpdateCustomerUseCase updateCustomerUseCase,
			CustomerRestMapper mapper) {
		this.createCustomerUseCase = createCustomerUseCase;
		this.updateCustomerUseCase = updateCustomerUseCase;
		this.mapper = mapper;
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
		Customer customer = createCustomerUseCase.execute(mapper.toCommand(request));
		return ResponseEntity
				.created(URI.create("/api/customers/" + customer.getId()))
				.body(mapper.toResponse(customer));
	}

	@PutMapping("/{id}")
	public ResponseEntity<CustomerResponse> update(
			@PathVariable String id,
			@Valid @RequestBody UpdateCustomerRequest request) {
		Customer customer = updateCustomerUseCase.execute(mapper.toCommand(id, request));
		return ResponseEntity.ok(mapper.toResponse(customer));
	}
}
