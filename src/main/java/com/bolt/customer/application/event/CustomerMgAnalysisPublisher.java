package com.bolt.customer.application.event;

public interface CustomerMgAnalysisPublisher {

	void publish(CustomerRegisteredInMgEvent event);
}
