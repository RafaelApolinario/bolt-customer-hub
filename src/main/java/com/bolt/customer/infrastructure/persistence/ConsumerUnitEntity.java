package com.bolt.customer.infrastructure.persistence;

import com.bolt.customer.domain.customer.Address;
import com.bolt.customer.domain.customer.ConsumerUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
		name = "consumer_units",
		uniqueConstraints = @UniqueConstraint(name = "uk_consumer_units_number", columnNames = "number"))
public class ConsumerUnitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String number;

	@Column(nullable = false, length = 8)
	private String zipCode;

	private String street;

	private String neighborhood;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false, length = 2)
	private String state;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerEntity customer;

	protected ConsumerUnitEntity() {
	}

	private ConsumerUnitEntity(String number, String zipCode, String street, String neighborhood, String city, String state) {
		this.number = number;
		this.zipCode = zipCode;
		this.street = street;
		this.neighborhood = neighborhood;
		this.city = city;
		this.state = state;
	}

	public static ConsumerUnitEntity fromDomain(ConsumerUnit consumerUnit) {
		Address address = consumerUnit.address();
		return new ConsumerUnitEntity(
				consumerUnit.number(),
				address.zipCode(),
				address.street(),
				address.neighborhood(),
				address.city(),
				address.state());
	}

	public ConsumerUnit toDomain() {
		return new ConsumerUnit(number, new Address(zipCode, street, neighborhood, city, state));
	}

	void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}
}
