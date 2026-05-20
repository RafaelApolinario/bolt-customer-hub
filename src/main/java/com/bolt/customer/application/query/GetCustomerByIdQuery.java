package com.bolt.customer.application.query;

import com.bolt.customer.domain.customer.CustomerId;

public record GetCustomerByIdQuery(CustomerId id) {
}
