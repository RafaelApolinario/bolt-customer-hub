package com.bolt.customer.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.bolt.customer.application.event.CustomerMgAnalysisPublisher;
import com.bolt.customer.application.event.CustomerRegisteredInMgEvent;

@Component
public class SpringCustomerMgEventPublisher implements CustomerMgAnalysisPublisher {

	private final ApplicationEventPublisher eventPublisher;

	public SpringCustomerMgEventPublisher(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void publish(CustomerRegisteredInMgEvent event) {
		eventPublisher.publishEvent(event);
	}
}
