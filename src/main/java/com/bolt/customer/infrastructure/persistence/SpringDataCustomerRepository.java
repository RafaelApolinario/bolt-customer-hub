package com.bolt.customer.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bolt.customer.domain.customer.CustomerStatus;

public interface SpringDataCustomerRepository extends JpaRepository<CustomerEntity, UUID> {

	@Override
	@EntityGraph(attributePaths = "consumerUnits")
	Optional<CustomerEntity> findById(UUID id);

	boolean existsByDocument(String document);

	boolean existsByDocumentAndIdNot(String document, UUID id);

	@EntityGraph(attributePaths = "consumerUnits")
	List<CustomerEntity> findByStatus(CustomerStatus status);

	@EntityGraph(attributePaths = "consumerUnits")
	List<CustomerEntity> findByStatusOrderByCreatedAtDesc(CustomerStatus status, Pageable pageable);

	@Query("select count(unit) > 0 from ConsumerUnitEntity unit where unit.number = :number")
	boolean existsConsumerUnitByNumber(@Param("number") String number);

	@Query("""
			select count(unit) > 0
			from ConsumerUnitEntity unit
			where unit.number = :number
			  and unit.customer.id <> :customerId
			""")
	boolean existsConsumerUnitByNumberAndCustomerIdNot(
			@Param("number") String number,
			@Param("customerId") UUID customerId);
}
