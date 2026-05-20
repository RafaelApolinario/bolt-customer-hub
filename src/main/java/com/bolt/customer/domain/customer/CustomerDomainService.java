package com.bolt.customer.domain.customer;

import java.util.Collection;
import java.util.Set;

import com.bolt.customer.domain.exception.BusinessException;

public class CustomerDomainService {

	private static final Set<String> BLOCKED_STATES = Set.of("SP", "RS", "PR");
	private static final String MG = "MG";

	public void ensureConsumerUnitsAreAllowed(Collection<ConsumerUnit> consumerUnits) {
		for (ConsumerUnit consumerUnit : consumerUnits) {
			if (BLOCKED_STATES.contains(consumerUnit.state())) {
				throw new BusinessException(
						"customer.consumer-unit.blocked-state",
						"Consumer units in SP, RS or PR are not allowed");
			}
		}
	}

	public boolean requiresMgAnalysis(Customer customer) {
		return customer.hasConsumerUnitInState(MG);
	}
}
