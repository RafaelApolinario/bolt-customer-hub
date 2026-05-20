package com.bolt.customer.application.command;

import com.bolt.customer.domain.customer.CustomerId;

public record DeleteCustomerCommand(CustomerId id) {
}
