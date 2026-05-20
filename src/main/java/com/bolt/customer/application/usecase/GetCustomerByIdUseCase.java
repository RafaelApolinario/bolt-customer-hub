package com.bolt.customer.application.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolt.customer.application.query.GetCustomerByIdQuery;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.exception.NotFoundException;

@Service
public class GetCustomerByIdUseCase {

	private final CustomerRepository customerRepository;

	public GetCustomerByIdUseCase(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Transactional(readOnly = true)
	public Customer execute(GetCustomerByIdQuery query) {
		return customerRepository.findById(query.id())
				.orElseThrow(() -> new NotFoundException("customer", "Customer not found"));
	}
}
