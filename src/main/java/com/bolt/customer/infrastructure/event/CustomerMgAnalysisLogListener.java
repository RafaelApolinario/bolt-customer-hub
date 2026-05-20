package com.bolt.customer.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.bolt.customer.application.event.CustomerRegisteredInMgEvent;

@Component
public class CustomerMgAnalysisLogListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerMgAnalysisLogListener.class);

	@EventListener
	public void on(CustomerRegisteredInMgEvent event) {
		LOGGER.info("Published event {} for customer {}", event.topic(), event.customerId());
	}
}
