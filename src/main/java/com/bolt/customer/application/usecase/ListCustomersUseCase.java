package com.bolt.customer.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolt.customer.application.query.ListCustomersQuery;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerRepository;

@Service
public class ListCustomersUseCase {

	private final CustomerRepository customerRepository;

	public ListCustomersUseCase(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Transactional(readOnly = true)
	public List<Customer> execute(ListCustomersQuery query) {
		return customerRepository.findAllActive();
	}
}
