package com.bolt.customer.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerId;
import com.bolt.customer.domain.customer.CustomerRepository;
import com.bolt.customer.domain.customer.CustomerStatus;
import com.bolt.customer.domain.customer.Document;

@Repository
public class JpaCustomerRepository implements CustomerRepository {

	private final SpringDataCustomerRepository repository;

	public JpaCustomerRepository(SpringDataCustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	public Customer save(Customer customer) {
		return repository.save(CustomerEntity.fromDomain(customer)).toDomain();
	}

	@Override
	public Optional<Customer> findById(CustomerId id) {
		return repository.findById(id.value()).map(CustomerEntity::toDomain);
	}

	@Override
	public boolean existsByDocument(Document document) {
		return repository.existsByDocument(document.value());
	}

	@Override
	public boolean existsByDocumentAndIdNot(Document document, CustomerId id) {
		return repository.existsByDocumentAndIdNot(document.value(), id.value());
	}

	@Override
	public boolean existsByConsumerUnitNumber(String number) {
		return repository.existsConsumerUnitByNumber(number);
	}

	@Override
	public boolean existsByConsumerUnitNumberAndCustomerIdNot(String number, CustomerId id) {
		return repository.existsConsumerUnitByNumberAndCustomerIdNot(number, id.value());
	}

	@Override
	public List<Customer> findAllActive() {
		return repository.findByStatus(CustomerStatus.ACTIVE).stream()
				.map(CustomerEntity::toDomain)
				.toList();
	}

	@Override
	public List<Customer> findLatestActive(int limit) {
		return repository.findByStatusOrderByCreatedAtDesc(CustomerStatus.ACTIVE, PageRequest.of(0, limit)).stream()
				.map(CustomerEntity::toDomain)
				.toList();
	}
}
