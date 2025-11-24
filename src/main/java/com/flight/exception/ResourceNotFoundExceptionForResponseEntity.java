package com.flight.exception;

public class ResourceNotFoundExceptionForResponseEntity extends RuntimeException {
	public ResourceNotFoundExceptionForResponseEntity(String msg) {
		super(msg);
	}
}