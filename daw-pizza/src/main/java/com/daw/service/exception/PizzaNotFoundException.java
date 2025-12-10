package com.daw.service.exception;

public class PizzaNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7239101262919209494L;

	public PizzaNotFoundException(String message) {
		super(message);
	}

}