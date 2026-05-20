package com.bolt.customer.application.usecase;

import java.time.Clock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolt.customer.application.command.DeleteCustomerCommand;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.exception.NotFoundException;

@Service
public class DeleteCustomerUseCase {

	private final CustomerRepository customerRepository;
	private final Clock clock;

	public DeleteCustomerUseCase(CustomerRepository customerRepository, Clock clock) {
		this.customerRepository = customerRepository;
		this.clock = clock;
	}

	@Transactional
	public void execute(DeleteCustomerCommand command) {
		Customer customer = customerRepository.findById(command.id())
				.orElseThrow(() -> new NotFoundException("customer", "Customer not found"));

		customer.deactivate(clock);
		customerRepository.save(customer);
	}
}
