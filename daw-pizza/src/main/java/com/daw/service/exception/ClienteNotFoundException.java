package com.daw.service.exception;

public class ClienteNotFoundException extends RuntimeException{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ClienteNotFoundException(String message) {
		super(message);
	}
}
