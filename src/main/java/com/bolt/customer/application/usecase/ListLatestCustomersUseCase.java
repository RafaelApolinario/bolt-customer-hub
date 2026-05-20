package com.bolt.customer.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolt.customer.application.query.ListLatestCustomersQuery;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerRepository;

@Service
public class ListLatestCustomersUseCase {

	private static final int MAX_LIMIT = 20;

	private final CustomerRepository customerRepository;

	public ListLatestCustomersUseCase(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Transactional(readOnly = true)
	public List<Customer> execute(ListLatestCustomersQuery query) {
		return customerRepository.findLatestActive(Math.min(query.limit(), MAX_LIMIT));
	}
}
