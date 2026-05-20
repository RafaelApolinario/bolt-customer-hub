package com.bolt.customer.domain.customer;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.bolt.customer.domain.exception.BusinessException;

public class Customer {

	private final CustomerId id;
	private String name;
	private Document document;
	private final List<ConsumerUnit> consumerUnits;
	private CustomerStatus status;
	private final Instant createdAt;
	private Instant updatedAt;

	private Customer(
			CustomerId id,
			String name,
			Document document,
			Collection<ConsumerUnit> consumerUnits,
			CustomerStatus status,
			Instant createdAt,
			Instant updatedAt) {
		this.id = Objects.requireNonNull(id, "id must not be null");
		this.name = normalizeName(name);
		this.document = requireDocument(document);
		this.consumerUnits = new ArrayList<>();
		replaceConsumerUnits(consumerUnits);
		this.status = Objects.requireNonNull(status, "status must not be null");
		this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
		this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
	}

	public static Customer create(String name, Document document, Collection<ConsumerUnit> consumerUnits) {
		return create(name, document, consumerUnits, Clock.systemUTC());
	}

	public static Customer create(String name, Document document, Collection<ConsumerUnit> consumerUnits, Clock clock) {
		Instant now = Instant.now(clock);
		return new Customer(CustomerId.generate(), name, document, consumerUnits, CustomerStatus.ACTIVE, now, now);
	}

	public static Customer restore(
			CustomerId id,
			String name,
			Document document,
			Collection<ConsumerUnit> consumerUnits,
			CustomerStatus status,
			Instant createdAt,
			Instant updatedAt) {
		return new Customer(id, name, document, consumerUnits, status, createdAt, updatedAt);
	}

	public void update(String name, Document document, Collection<ConsumerUnit> consumerUnits) {
		update(name, document, consumerUnits, Clock.systemUTC());
	}

	public void update(String name, Document document, Collection<ConsumerUnit> consumerUnits, Clock clock) {
		this.name = normalizeName(name);
		this.document = requireDocument(document);
		replaceConsumerUnits(consumerUnits);
		touch(clock);
	}

	public void deactivate() {
		deactivate(Clock.systemUTC());
	}

	public void deactivate(Clock clock) {
		this.status = CustomerStatus.INACTIVE;
		touch(clock);
	}

	public boolean isActive() {
		return status == CustomerStatus.ACTIVE;
	}

	public boolean hasConsumerUnitInState(String state) {
		return consumerUnits.stream().anyMatch(unit -> unit.state().equalsIgnoreCase(state));
	}

	public CustomerId getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Document getDocument() {
		return document;
	}

	public List<ConsumerUnit> getConsumerUnits() {
		return Collections.unmodifiableList(consumerUnits);
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	private void replaceConsumerUnits(Collection<ConsumerUnit> units) {
		if (units == null || units.isEmpty()) {
			throw new BusinessException("customer.consumer-units.required", "Customer must have at least one consumer unit");
		}
		this.consumerUnits.clear();
		this.consumerUnits.addAll(units);
	}

	private void touch(Clock clock) {
		this.updatedAt = Instant.now(clock);
	}

	private static String normalizeName(String value) {
		if (value == null || value.isBlank()) {
			throw new BusinessException("customer.name.required", "Customer name is required");
		}
		return value.trim();
	}

	private static Document requireDocument(Document document) {
		if (document == null) {
			throw new BusinessException("customer.document.required", "Customer document is required");
		}
		return document;
	}
}
