package com.example.schoolservice.exception;

public class SchoolExitsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchoolExitsException() {
		super();
	}

	public SchoolExitsException(String message, Throwable cause) {
		super(message, cause);
	}

	public SchoolExitsException(String message) {
		super(message);
	}

	public SchoolExitsException(Throwable cause) {
		super(cause);
	}

}
