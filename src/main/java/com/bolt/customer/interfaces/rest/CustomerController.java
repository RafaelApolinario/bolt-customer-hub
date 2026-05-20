package com.bolt.customer.interfaces.rest;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolt.customer.application.usecase.CreateCustomerUseCase;
import com.bolt.customer.application.usecase.DeleteCustomerUseCase;
import com.bolt.customer.application.usecase.GetCustomerByIdUseCase;
import com.bolt.customer.application.usecase.ListCustomersUseCase;
import com.bolt.customer.application.usecase.ListLatestCustomersUseCase;
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
	private final DeleteCustomerUseCase deleteCustomerUseCase;
	private final GetCustomerByIdUseCase getCustomerByIdUseCase;
	private final ListCustomersUseCase listCustomersUseCase;
	private final ListLatestCustomersUseCase listLatestCustomersUseCase;
	private final CustomerRestMapper mapper;

	public CustomerController(
			CreateCustomerUseCase createCustomerUseCase,
			UpdateCustomerUseCase updateCustomerUseCase,
			DeleteCustomerUseCase deleteCustomerUseCase,
			GetCustomerByIdUseCase getCustomerByIdUseCase,
			ListCustomersUseCase listCustomersUseCase,
			ListLatestCustomersUseCase listLatestCustomersUseCase,
			CustomerRestMapper mapper) {
		this.createCustomerUseCase = createCustomerUseCase;
		this.updateCustomerUseCase = updateCustomerUseCase;
		this.deleteCustomerUseCase = deleteCustomerUseCase;
		this.getCustomerByIdUseCase = getCustomerByIdUseCase;
		this.listCustomersUseCase = listCustomersUseCase;
		this.listLatestCustomersUseCase = listLatestCustomersUseCase;
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

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		deleteCustomerUseCase.execute(mapper.toDeleteCommand(id));
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<CustomerResponse>> list() {
		List<CustomerResponse> customers = listCustomersUseCase.execute(mapper.toListCustomersQuery()).stream()
				.map(mapper::toResponse)
				.toList();
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/latest")
	public ResponseEntity<List<CustomerResponse>> latest() {
		List<CustomerResponse> customers = listLatestCustomersUseCase.execute(mapper.toListLatestCustomersQuery()).stream()
				.map(mapper::toResponse)
				.toList();
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponse> getById(@PathVariable String id) {
		Customer customer = getCustomerByIdUseCase.execute(mapper.toGetByIdQuery(id));
		return ResponseEntity.ok(mapper.toResponse(customer));
	}
}
