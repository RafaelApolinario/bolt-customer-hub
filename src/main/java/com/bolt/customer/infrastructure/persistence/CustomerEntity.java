package com.bolt.customer.infrastructure.persistence;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.bolt.customer.domain.customer.ConsumerUnit;
import com.bolt.customer.domain.customer.Customer;
import com.bolt.customer.domain.customer.CustomerId;
import com.bolt.customer.domain.customer.CustomerStatus;
import com.bolt.customer.domain.customer.Document;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "customers",
		uniqueConstraints = @UniqueConstraint(name = "uk_customers_document", columnNames = "document"))
public class CustomerEntity {

	@Id
	private UUID id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String document;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CustomerStatus status;

	@Column(nullable = false)
	private Instant createdAt;

	@Column(nullable = false)
	private Instant updatedAt;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ConsumerUnitEntity> consumerUnits = new ArrayList<>();

	protected CustomerEntity() {
	}

	public static CustomerEntity fromDomain(Customer customer) {
		CustomerEntity entity = new CustomerEntity();
		entity.updateFromDomain(customer);
		return entity;
	}

	public void updateFromDomain(Customer customer) {
		this.id = customer.getId().value();
		this.name = customer.getName();
		this.document = customer.getDocument().value();
		this.status = customer.getStatus();
		this.createdAt = customer.getCreatedAt();
		this.updatedAt = customer.getUpdatedAt();
		this.replaceConsumerUnits(customer.getConsumerUnits());
	}

	public Customer toDomain() {
		return Customer.restore(
				CustomerId.from(id),
				name,
				Document.of(document),
				consumerUnits.stream().map(ConsumerUnitEntity::toDomain).toList(),
				status,
				createdAt,
				updatedAt);
	}

	private void replaceConsumerUnits(List<ConsumerUnit> units) {
		Set<String> requestedNumbers = units.stream()
				.map(ConsumerUnit::number)
				.collect(Collectors.toSet());

		this.consumerUnits.removeIf(entity -> !requestedNumbers.contains(entity.getNumber()));

		for (ConsumerUnit unit : units) {
			ConsumerUnitEntity entity = findConsumerUnitByNumber(unit.number());
			if (entity == null) {
				entity = ConsumerUnitEntity.fromDomain(unit);
				this.consumerUnits.add(entity);
			} else {
				entity.updateFromDomain(unit);
			}
			entity.setCustomer(this);
		}
	}

	private ConsumerUnitEntity findConsumerUnitByNumber(String number) {
		return this.consumerUnits.stream()
				.filter(entity -> entity.hasNumber(number))
				.findFirst()
				.orElse(null);
	}
}
