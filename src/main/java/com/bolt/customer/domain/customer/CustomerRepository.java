package com.bolt.customer.domain.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

	Customer save(Customer customer);

	Optional<Customer> findById(CustomerId id);

	boolean existsByDocument(Document document);

	boolean existsByDocumentAndIdNot(Document document, CustomerId id);

	boolean existsByConsumerUnitNumber(String number);

	boolean existsByConsumerUnitNumberAndCustomerIdNot(String number, CustomerId id);

	List<Customer> findAllActive();

	List<Customer> findLatestActive(int limit);
}
