package com.bolt.customer.application.gateway;

import com.bolt.customer.domain.customer.Address;

public interface AddressGateway {

	Address findByZipCode(String zipCode);
}
