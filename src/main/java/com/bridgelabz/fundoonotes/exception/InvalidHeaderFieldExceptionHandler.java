package com.bridgelabz.fundoonotes.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bridgelabz.fundoonotes.response.ExceptionHandlerResponse;

@ControllerAdvice
public class InvalidHeaderFieldExceptionHandler {
	@ExceptionHandler(InvalidHeaderFieldException.class)
	public final ResponseEntity<ExceptionHandlerResponse> invalidHeaderFieldException(InvalidHeaderFieldException ex){
		ExceptionHandlerResponse exp=new ExceptionHandlerResponse();
		exp.setMessage(ex.getStatusMessage());
		exp.setCode(ex.getStatusCode());
		exp.setTime(LocalDateTime.now());
		return ResponseEntity.status(exp.getCode()).body(new ExceptionHandlerResponse(exp.getMessage(), exp.getCode(), exp.getTime()));
	}
}
