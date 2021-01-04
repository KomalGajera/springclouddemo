package com.example.schoolservice.exception;

public class SchoolNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchoolNotFoundException() {
		super();
	}

	public SchoolNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchoolNotFoundException(String message) {
		super(message);
	}

	public SchoolNotFoundException(Throwable cause) {
		super(cause);
	}

}
