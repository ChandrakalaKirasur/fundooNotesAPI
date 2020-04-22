package com.bridgelabz.fundoonotes.exception;

import org.springframework.http.HttpStatus;

public class UserMailException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	private HttpStatus status;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public UserMailException(String message,HttpStatus status) {
		this.message=message;
		this.status=status;
	}
}
