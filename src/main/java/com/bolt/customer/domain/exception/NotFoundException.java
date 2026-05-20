package com.bolt.customer.domain.exception;

public class NotFoundException extends RuntimeException {

	private final String resource;

	public NotFoundException(String resource, String message) {
		super(message);
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}
}
