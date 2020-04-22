package com.bridgelabz.fundoonotes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class LabelException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String statusMessage;
	private int statusCode;
	
}
