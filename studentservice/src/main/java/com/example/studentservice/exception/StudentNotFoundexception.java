package com.example.studentservice.exception;

public class StudentNotFoundexception extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StudentNotFoundexception() {
		super();
	}

	public StudentNotFoundexception(String message, Throwable cause) {
		super(message, cause);
	}

	public StudentNotFoundexception(String message) {
		super(message);
	}

	public StudentNotFoundexception(Throwable cause) {
		super(cause);
	}

}
